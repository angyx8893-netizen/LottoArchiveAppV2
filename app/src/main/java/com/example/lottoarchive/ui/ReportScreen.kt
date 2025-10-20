package com.example.lottoarchive.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottoarchive.vm.LottoViewModel

@Composable
fun ReportScreen(vm: LottoViewModel = viewModel()) {
    var wheel by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Seleziona opzioni e genera un PDF") }

    val creator = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri: Uri? ->
        if (uri != null) {
            vm.exportPdf(uri, wheel.ifBlank { null }) { ok, msg -> status = if (ok) "OK: $msg" else "Errore: $msg" }
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = wheel, onValueChange = { wheel = it }, label = { Text("Ruota (vuoto = tutte)") })
        Spacer(Modifier.height(8.dp))
        Button(onClick = { creator.launch("LottoReport.pdf") }) { Text("Genera PDF") }
        Spacer(Modifier.height(12.dp))
        Text(status)
    }
}
