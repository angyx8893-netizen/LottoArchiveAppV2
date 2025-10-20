package com.example.lottoarchive.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GuideScreen() {
    val text = """
        Guida rapida
        ============
        Importa: usa un CSV con intestazione date,wheel,n1,n2,n3,n4,n5.
        Statistiche: calcola frequenze, ultima uscita e combinazioni (ambi/terni).
        Script: JavaScript nativo (Rhino) o VBS (compatibilità base).
        Funzioni script: countOccurrences(num, wheel), lastSeen(num, wheel),
        topFrequencies(n, wheel), comboFrequency("1,2,3", wheel), randomNumbers(n).

        PDF: Genera un report con istogramma delle frequenze (Top 20).
        Backup: salva e ripristina l'intero database (zip).
    """.trimIndent()
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(text)
    }
}
