package com.example.tvtube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import androidx.lifecycle.lifecycleScope
import com.example.tvtube.ui.TestSearchScreen
import com.example.tvtube.ui.UpdateScreen
import com.example.tvtube.updater.OtaUpdater
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var isDownloading by mutableStateOf(false)
    private var currentScreen by mutableStateOf("update") // Default to UpdateScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (currentScreen == "update") {
                        UpdateScreen(
                            currentVersion = "1.0.1",
                            latestVersion = "Latest",
                            isDownloading = isDownloading,
                            onInstallUpdate = { triggerUpdate() }
                        )
                    } else {
                        TestSearchScreen()
                    }

                    // Navigation toggle in the bottom-end corner
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Button(onClick = {
                            currentScreen = if (currentScreen == "update") "search" else "update"
                        }) {
                            Text(text = if (currentScreen == "update") "Switch to Search Test" else "Switch to Update Screen")
                        }
                    }
                }
            }
        }
    }

    private fun triggerUpdate() {
        if (isDownloading) return
        isDownloading = true
        lifecycleScope.launch {
            try {
                OtaUpdater.downloadAndInstall(
                    this@MainActivity,
                    "https://github.com/jch0029987-glitch/tvtube/releases/latest/download/app-release.apk"
                )
            } finally {
                isDownloading = false
            }
        }
    }
}
