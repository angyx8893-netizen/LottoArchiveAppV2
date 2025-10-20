package com.lottoarchiveapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoArchiveApp()
        }
    }
}

@Composable
fun LottoArchiveApp() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("LottoArchive") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Benvenuto in LottoArchive!", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("L’app è pronta per essere testata.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLottoArchiveApp() {
    LottoArchiveApp()
}
