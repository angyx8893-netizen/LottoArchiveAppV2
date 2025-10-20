package com.example.lottoarchive.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import java.io.InputStream
import java.io.OutputStream

object FileUtils {
    fun copyUriToUri(cr: ContentResolver, src: Uri, dst: Uri): Boolean {
        return try {
            cr.openInputStream(src).use { input ->
                cr.openOutputStream(dst).use { output ->
                    if (input == null || output == null) return false
                    input.copyTo(output)
                    true
                }
            }
        } catch (_: Exception) { false }
    }

    fun writeTextPdf(ctx: Context, uri: Uri, title: String, bodyLines: List<String>, histogram: List<Pair<Int,Int>>?) : Boolean {
        return try {
            val doc = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 ~ 72dpi
            val page = doc.startPage(pageInfo)
            val c = page.canvas
            val paint = Paint().apply { color = Color.BLACK; textSize = 14f; isAntiAlias = true }
            var y = 40f
            paint.textSize = 18f
            c.drawText(title, 40f, y, paint); y += 24f
            paint.textSize = 12f
            bodyLines.forEach {
                for (line in wrapText(it, 80)) {
                    c.drawText(line, 40f, y, paint); y += 16f
                }
            }

            if (histogram != null && histogram.isNotEmpty()) {
                y += 16f
                paint.textSize = 14f
                c.drawText("Istogramma frequenze (Top ${histogram.size})", 40f, y, paint); y += 8f
                val maxVal = histogram.maxOf { it.second }.coerceAtLeast(1)
                val barW = 10f
                var x = 40f
                val baseY = y + 200f
                // axis
                c.drawLine(40f, baseY, 560f, baseY, paint)
                histogram.forEach { (num, cnt) ->
                    val h = (cnt.toFloat() / maxVal) * 180f
                    c.drawRect(x, baseY - h, x + barW, baseY, paint)
                    c.drawText(num.toString(), x, baseY + 12f, paint)
                    x += barW + 6f
                    if (x > 540f) { y = baseY + 40f; x = 40f }
                }
                y = baseY + 60f
            }

            doc.finishPage(page)
            ctx.contentResolver.openOutputStream(uri).use { out ->
                if (out == null) return false
                doc.writeTo(out)
            }
            doc.close()
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun wrapText(text: String, maxChars: Int): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var cur = StringBuilder()
        for (w in words) {
            if (cur.length + w.length + 1 > maxChars) {
                lines.add(cur.toString())
                cur = StringBuilder()
            }
            if (cur.isNotEmpty()) cur.append(' ')
            cur.append(w)
        }
        if (cur.isNotEmpty()) lines.add(cur.toString())
        return lines
    }
}
