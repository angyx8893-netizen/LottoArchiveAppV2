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
fun SearchScreen(vm: LottoViewModel = viewModel()) {
    var wheel by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    val results by vm.searchResults.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = wheel, onValueChange = { wheel = it }, label = { Text("Ruota (es. Bari)") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Numero (1-90)") })
        Spacer(Modifier.height(8.dp))
        Button(onClick = { vm.search(wheel.ifBlank { null }, number.toIntOrNull()) }) { Text("Cerca") }
        Spacer(Modifier.height(8.dp))
        LazyColumn {
            items(results) { d ->
                ListItem(
                    headlineContent = { Text("${d.date} - ${d.wheel}") },
                    supportingContent = { Text("Numeri: ${listOf(d.n1,d.n2,d.n3,d.n4,d.n5).joinToString(\", \")}") }
                )
                Divider()
            }
        }
    }
}
