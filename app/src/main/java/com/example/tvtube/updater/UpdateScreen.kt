package com.example.tvtube.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Surface
import androidx.tv.material3.Text

@Composable
fun UpdateScreen(
    currentVersion: String,
    latestVersion: String,
    isDownloading: Boolean,
    onInstallUpdate: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "TVTube Update Available")
                Text(text = "Installed Version: $currentVersion")
                Text(text = "Latest Version: $latestVersion")

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onInstallUpdate,
                    enabled = !isDownloading
                ) {
                    Text(text = if (isDownloading) "Downloading Update..." else "Download & Install")
                }
            }
        }
    }
}
