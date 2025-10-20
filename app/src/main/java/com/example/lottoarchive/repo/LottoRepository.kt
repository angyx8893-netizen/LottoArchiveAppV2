package com.example.lottoarchive.repo

import android.content.Context
import com.example.lottoarchive.db.*
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class LottoRepository(private val ctx: Context, private val dao: DrawDao) {
    suspend fun exportPdf(uri: android.net.Uri, wheel: String?): Pair<Boolean,String> = withContext(Dispatchers.IO) {
        try {
            val list = all().first().filter { wheel == null || it.wheel == wheel }
            val counts = IntArray(91)
            list.forEach { d -> listOf(d.n1,d.n2,d.n3,d.n4,d.n5).forEach { counts[it]++ } }
            val top = (1..90).map { it to counts[it] }.sortedByDescending { it.second }.take(20)

            val lines = mutableListOf<String>()
            lines += "Ruota: " + (wheel ?: "tutte")
            lines += "Totale estrazioni considerate: ${list.size}"
            lines += "Top frequenze:"
            lines += top.joinToString("  ") { "${it.first}:${it.second}" }

            val ok = com.example.lottoarchive.util.FileUtils.writeTextPdf(ctx, uri, "Report Lotto", lines, top)
            ok to (if (ok) "Report creato" else "Impossibile scrivere PDF")
        } catch (e: Exception) {
            false to (e.message ?: "Errore")
        }
    }

    private fun dbPath(): java.io.File =
        ctx.getDatabasePath("lotto.db")

    suspend fun backupTo(uri: android.net.Uri): Pair<Boolean,String> = withContext(Dispatchers.IO) {
        try {
            ctx.contentResolver.openOutputStream(uri).use { out ->
                if (out == null) return@withContext false to "Output nullo"
                java.util.zip.ZipOutputStream(out).use { zip ->
                    val entry = java.util.zip.ZipEntry("lotto.db")
                    zip.putNextEntry(entry)
                    dbPath().inputStream().use { it.copyTo(zip) }
                    zip.closeEntry()
                }
            }
            true to "Backup creato"
        } catch (e: Exception) {
            false to (e.message ?: "Errore backup")
        }
    }

    suspend fun restoreFrom(uri: android.net.Uri): Pair<Boolean,String> = withContext(Dispatchers.IO) {
        try {
            // Expect zip with lotto.db at root
            ctx.contentResolver.openInputStream(uri).use { inp ->
                if (inp == null) return@withContext false to "Input nullo"
                java.util.zip.ZipInputStream(inp).use { zip ->
                    var e = zip.nextEntry
                    var ok = false
                    while (e != null) {
                        if (!e.isDirectory && e.name.endsWith("lotto.db")) {
                            val dst = dbPath()
                            // Close DB to avoid lock (best-effort)
                            zip.copyTo(dst.outputStream())
                            ok = true
                            break
                        }
                        e = zip.nextEntry
                    }
                    if (!ok) return@withContext false to "lotto.db non trovato nel backup"
                }
            }
            true to "Database ripristinato. Riavvia l'app."
        } catch (e: Exception) {
            false to (e.message ?: "Errore ripristino")
        }
    }

    suspend fun importCsvFromUri(uri: android.net.Uri): Pair<Boolean,String> = withContext(Dispatchers.IO) {
        try {
            ctx.contentResolver.openInputStream(uri).use { input ->
                if (input == null) return@withContext false to "Impossibile aprire il file"
                CSVReader(InputStreamReader(input)).use { reader ->
                    val rows = reader.readAll()
                    if (rows.isEmpty()) return@withContext false to "File vuoto"
                    val header = rows.first().map { it.lowercase() }
                    val expected = listOf("date","wheel","n1","n2","n3","n4","n5")
                    if (header != expected) return@withContext false to "Intestazione errata. Attesa: " + expected.joinToString(",")
                    val draws = rows.drop(1).mapNotNull { r ->
                        try {
                            Draw(
                                date = r[0],
                                wheel = r[1],
                                n1 = r[2].toInt(), n2 = r[3].toInt(), n3 = r[4].toInt(), n4 = r[5].toInt(), n5 = r[6].toInt()
                            )
                        } catch (_: Exception) { null }
                    }
                    dao.clear()
                    dao.insertAll(draws)
                    true to "Importate ${draws.size} righe"
                }
            }
        } catch (e: Exception) {
            false to (e.message ?: "Errore sconosciuto")
        }
    }

    suspend fun countNumber(number: Int, wheel: String?) = dao.countNumber(number, wheel)
    suspend fun lastDateForNumber(number: Int, wheel: String?) = dao.lastDateForNumber(number, wheel)
    suspend fun maxDate() = dao.maxDate()

    suspend fun topFrequencies(limit: Int = 10, wheel: String? = null): List<Pair<Int,Int>> = withContext(Dispatchers.IO) {
        val counts = IntArray(91)
        all().first().forEach { d ->
            if (wheel == null || d.wheel == wheel) {
                listOf(d.n1,d.n2,d.n3,d.n4,d.n5).forEach { counts[it]++ }
            }
        }
        (1..90).map { it to counts[it] }.sortedByDescending { it.second }.take(limit)
    }

    suspend fun comboFrequency(nums: List<Int>, wheel: String?): Int = withContext(Dispatchers.IO) {
        if (nums.isEmpty()) return@withContext 0
        var c = 0
        all().first().forEach { d ->
            if (wheel == null || d.wheel == wheel) {
                val set = setOf(d.n1,d.n2,d.n3,d.n4,d.n5)
                if (nums.all { it in set }) c++
            }
        }
        c
    }


    fun all(): Flow<List<Draw>> = dao.all()
    fun search(wheel: String?, number: Int?) = dao.search(wheel, number)

    suspend fun clearAll() = dao.clear()

    suspend fun loadSampleFromAssets() = withContext(Dispatchers.IO) {
        ctx.assets.open("sample_lotto.csv").use { input ->
            CSVReader(InputStreamReader(input)).use { reader ->
                val rows = reader.readAll().drop(1) // skip header
                val draws = rows.mapNotNull { r ->
                    try {
                        Draw(
                            date = r[0],
                            wheel = r[1],
                            n1 = r[2].toInt(), n2 = r[3].toInt(), n3 = r[4].toInt(), n4 = r[5].toInt(), n5 = r[6].toInt()
                        )
                    } catch (e: Exception) { null }
                }
                dao.insertAll(draws)
            }
        }
    }

    suspend fun countOccurrences(number: Int, wheel: String?): Int =
        dao.countOccurrences(number, wheel)
}
