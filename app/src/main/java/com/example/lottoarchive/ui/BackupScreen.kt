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
fun BackupScreen(vm: LottoViewModel = viewModel()) {
    var status by remember { mutableStateOf("Esegui backup o ripristino del DB") }

    val createZip = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/zip")) { uri: Uri? ->
        if (uri != null) vm.backupTo(uri) { ok, msg -> status = if (ok) "Backup OK: $msg" else "Backup errore: $msg" }
    }
    val openZip = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) vm.restoreFrom(uri) { ok, msg -> status = if (ok) "Ripristino OK: $msg (riavvia app)" else "Ripristino errore: $msg" }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            Button(onClick = { createZip.launch("lotto_backup.zip") }) { Text("Backup") }
            Spacer(Modifier.width(12.dp))
            Button(onClick = { openZip.launch(arrayOf("application/zip","application/octet-stream")) }) { Text("Ripristina") }
        }
        Spacer(Modifier.height(12.dp))
        Text(status)
    }
}
