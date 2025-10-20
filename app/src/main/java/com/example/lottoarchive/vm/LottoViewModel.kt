package com.example.lottoarchive.vm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottoarchive.db.LottoDb
import com.example.lottoarchive.repo.LottoRepository
import com.example.lottoarchive.scripting.ScriptHost
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LottoViewModel(app: Application) : AndroidViewModel(app) {
    
    private val repo = LottoRepository(app, LottoDb.get(app).drawDao())
    private val host = ScriptHost(repo)

    val allDraws = repo.all().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val _searchResults = MutableStateFlow(emptyList<com.example.lottoarchive.db.Draw>())
    val searchResults: StateFlow<List<com.example.lottoarchive.db.Draw>> = _searchResults

    private val _scriptOutput = MutableStateFlow("")
    val scriptOutput: StateFlow<String> = _scriptOutput

    private val _stats = MutableStateFlow("")
    val stats: StateFlow<String> = _stats

    private val _comboStats = MutableStateFlow("")
    val comboStats: StateFlow<String> = _comboStats


    fun loadSample() = viewModelScope.launch { repo.loadSampleFromAssets() }
    fun clearAll() = viewModelScope.launch { repo.clearAll() }

    fun search(wheel: String?, number: Int?) {
        viewModelScope.launch { repo.search(wheel, number).collect { _searchResults.value = it } }
    }

    fun runScript(code: String, vbsMode: Boolean) {
        viewModelScope.launch {
            val out = if (vbsMode) host.runVbsLike(code) else host.runJs(code)
            _scriptOutput.value = out
        }
    }

    fun importCsv(uri: Uri, onDone: (Boolean, String)->Unit) {
        viewModelScope.launch {
            val (ok,msg) = repo.importCsvFromUri(uri)
            onDone(ok,msg)
        }
    }

    fun computeStats(number: Int?, wheel: String?) {
        viewModelScope.launch {
            if (number == null) { _stats.value = "Inserisci un numero valido"; return@launch }
            val freq = repo.countNumber(number, wheel)
            val last = repo.lastDateForNumber(number, wheel) ?: "mai"
            val max = repo.maxDate() ?: "n.d."
            val text = "Numero $number" + (if (wheel!=null) " su $wheel" else " su tutte") +
                "\nFrequenza: $freq\nUltima uscita: $last\nData più recente nel DB: $max"
            _stats.value = text
        }
    }

    fun topFrequencies(wheel: String?) {
        viewModelScope.launch {
            val top = repo.topFrequencies(10, wheel)
            _stats.value = "Top frequenze " + (wheel?.let { "su $it" } ?: "su tutte") +
                ":\n" + top.joinToString("\n") { "${it.first}: ${it.second}" }
        }
    }

    fun comboFrequency(nums: List<Int>, wheel: String?) {
        viewModelScope.launch {
            val c = repo.comboFrequency(nums, wheel)
            _comboStats.value = "Combinazione ${nums.joinToString(\",\")} " + (wheel?.let { "su $it" } ?: "su tutte") + ": $c occorrenze"
        }
    }
}


    fun exportPdf(uri: android.net.Uri, wheel: String?, onDone: (Boolean,String)->Unit) {
        viewModelScope.launch {
            val (ok,msg) = repo.exportPdf(uri, wheel)
            onDone(ok,msg)
        }
    }

    fun backupTo(uri: android.net.Uri, onDone: (Boolean,String)->Unit) {
        viewModelScope.launch {
            val (ok,msg) = repo.backupTo(uri)
            onDone(ok,msg)
        }
    }

    fun restoreFrom(uri: android.net.Uri, onDone: (Boolean,String)->Unit) {
        viewModelScope.launch {
            val (ok,msg) = repo.restoreFrom(uri)
            onDone(ok,msg)
        }
    }
