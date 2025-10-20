package com.example.lottoarchive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lottoarchive.ui.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.MenuBook

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                Scaffold(
                    topBar = {
                        SmallTopAppBar(title = { Text("Lotto Archive") })
                    },
                    bottomBar = {
                        NavigationBar {
                            val current = nav.currentBackStackEntryFlow.collectAsState(initial = nav.currentBackStackEntry).value?.destination?.route
                            NavigationBarItem(selected = current == "archive", onClick = { nav.navigate("archive") }, icon = { Icon(Icons.Filled.List, contentDescription = "Archivio") }, label = { Text("Archivio") })
                            NavigationBarItem(selected = current == "search", onClick = { nav.navigate("search") }, icon = { Icon(Icons.Filled.Search, contentDescription = "Cerca") }, label = { Text("Cerca") })
                            NavigationBarItem(selected = current == "import", onClick = { nav.navigate("import") }, icon = { Icon(Icons.Filled.InsertDriveFile, contentDescription = "Importa") }, label = { Text("Importa") })
                            NavigationBarItem(selected = current == "stats", onClick = { nav.navigate("stats") }, icon = { Icon(Icons.Filled.Insights, contentDescription = "Statistiche") }, label = { Text("Statistiche") })
                            NavigationBarItem(selected = current == "report", onClick = { nav.navigate("report") }, icon = { Icon(Icons.Filled.PictureAsPdf, contentDescription = "Report") }, label = { Text("Report") })
                            NavigationBarItem(selected = current == "backup", onClick = { nav.navigate("backup") }, icon = { Icon(Icons.Filled.Backup, contentDescription = "Backup") }, label = { Text("Backup") })
                            NavigationBarItem(selected = current == "scripts", onClick = { nav.navigate("scripts") }, icon = { Icon(Icons.Filled.Code, contentDescription = "Script") }, label = { Text("Script") })
                            NavigationBarItem(selected = current == "guide", onClick = { nav.navigate("guide") }, icon = { Icon(Icons.Filled.MenuBook, contentDescription = "Guida") }, label = { Text("Guida") })
                        }
                    }
                ) { padding ->
                    Column(Modifier.fillMaxSize()) {
                        NavHost(navController = nav, startDestination = "archive", modifier = Modifier.weight(1f).padding(padding)) {
                            composable("archive") { ArchiveScreen() }
                            composable("search") { SearchScreen() }
                            composable("import") { ImportScreen() }
                            composable("stats") { StatsScreen() }
                            composable("report") { ReportScreen() }
                            composable("backup") { BackupScreen() }
                            composable("scripts") { ScriptScreen() }
                            composable("guide") { GuideScreen() }
                        }
                        Divider()
                        val buildInfo = "Build: " + (BuildConfig.BUILD_TIME ?: "DEV") + " • Key: " + BuildConfig.KEY_ALIAS + " • v" + BuildConfig.VERSION_NAME
                        Text(buildInfo, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
