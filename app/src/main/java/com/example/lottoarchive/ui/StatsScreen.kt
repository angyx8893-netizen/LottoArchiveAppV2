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

@Composable
fun StatsScreen(vm: LottoViewModel = viewModel()) {
    var wheel by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var comboInput by remember { mutableStateOf("1,2") }
    val stats by vm.stats.collectAsState()
    val combos by vm.comboStats.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Statistiche numero", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = wheel, onValueChange = { wheel = it }, label = { Text("Ruota (vuoto = tutte)") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Numero (1-90)") })
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = { vm.computeStats(number.toIntOrNull(), wheel.ifBlank { null }) }) { Text("Calcola") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { vm.topFrequencies(wheel.ifBlank { null }) }) { Text("Top frequenze") }
        }
        Spacer(Modifier.height(12.dp))
        Text(stats, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))
        Text("Frequenze combinazioni (ambi/terni)", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = comboInput, onValueChange = { comboInput = it }, label = { Text("Numeri separati da virgola (es. 1,2 o 1,2,3)") })
        Spacer(Modifier.height(8.dp))
        Button(onClick = { vm.comboFrequency(comboInput.split(",").mapNotNull { it.trim().toIntOrNull() }, wheel.ifBlank { null }) }) { Text("Conta combinazione") }
        Spacer(Modifier.height(12.dp))
        Text(combos)
    }
}
