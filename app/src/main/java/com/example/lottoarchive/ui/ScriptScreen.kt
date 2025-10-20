package com.example.lottoarchive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottoarchive.vm.LottoViewModel

@Composable
fun ScriptScreen(vm: LottoViewModel = viewModel()) {
    var code by remember { mutableStateOf("// JS o VBS (base). Esempio:\n// countOccurrences(90, \"Bari\")\nprint(countOccurrences(90, \"Bari\"))") }
    val output by vm.scriptOutput.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            modifier = Modifier.fillMaxWidth().weight(1f),
            label = { Text("Script") }
        )
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = { vm.runScript(code, vbsMode = false) }) { Text("Esegui come JS") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { vm.runScript(code, vbsMode = true) }) { Text("Esegui come VBS (beta)") }
        }
        Spacer(Modifier.height(8.dp))
        Text("Output:", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = output, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth().weight(1f))
    }
}
