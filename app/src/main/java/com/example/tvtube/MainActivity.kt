package com.example.tvtube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.tv.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.example.tvtube.ui.UpdateScreen
import com.example.tvtube.updater.OtaUpdater
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var isDownloading by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                UpdateScreen(
                    currentVersion = "1.0.1",
                    latestVersion = "Latest",
                    isDownloading = isDownloading,
                    onInstallUpdate = {
                        triggerUpdate()
                    }
                )
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
