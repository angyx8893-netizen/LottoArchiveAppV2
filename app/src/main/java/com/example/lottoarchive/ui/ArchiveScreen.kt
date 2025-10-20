package com.example.lottoarchive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lottoarchive.vm.LottoViewModel
import com.example.lottoarchive.db.Draw

@Composable
fun ArchiveScreen(vm: LottoViewModel = viewModel()) {
    val draws by vm.allDraws.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { vm.loadSample() }) { Text("Carica esempio") }
            Button(onClick = { vm.clearAll() }) { Text("Svuota") }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(draws) { d ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${d.date} - ${d.wheel}", style = MaterialTheme.typography.titleMedium)
                        Text("Numeri: ${listOf(d.n1,d.n2,d.n3,d.n4,d.n5).joinToString(\", \")}")
                    }
                }
            }
        }
    }
}
