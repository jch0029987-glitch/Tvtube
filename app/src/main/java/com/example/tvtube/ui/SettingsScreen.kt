package com.example.tvtube.ui

import androidx.compose.foundation.layout.*
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onCheckForUpdates: () -> Unit, onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Settings")
            Button(onClick = onCheckForUpdates) {
                Text(text = "Check for App Updates")
            }
            Button(onClick = onBack) {
                Text(text = "Back to Home")
            }
        }
    }
}
