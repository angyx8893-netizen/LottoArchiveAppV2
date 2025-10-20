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
fun ImportScreen(vm: LottoViewModel = viewModel()) {
    var lastMsg by remember { mutableStateOf("Seleziona un CSV con intestazione: date,wheel,n1,n2,n3,n4,n5") }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            vm.importCsv(uri) { ok, msg ->
                lastMsg = if (ok) "Import riuscito: $msg" else "Errore import: $msg"
            }
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { picker.launch(arrayOf("text/*", "text/csv", "application/csv")) }) {
            Text("Scegli CSV")
        }
        Spacer(Modifier.height(12.dp))
        Text(lastMsg)
    }
}
