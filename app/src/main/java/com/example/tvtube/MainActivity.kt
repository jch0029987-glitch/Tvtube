package com.example.tvtube

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.example.tvtube.ui.SettingsScreen
import com.example.tvtube.updater.OtaUpdater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            var currentScreen by remember { mutableStateOf("home") }
            val scope = rememberCoroutineScope()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                when (currentScreen) {
                    "home" -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "TVTube Home Dashboard", color = Color.White)
                            Button(onClick = { currentScreen = "settings" }) {
                                Text(text = "Open Settings")
                            }
                        }
                    }
                    "settings" -> {
                        SettingsScreen(
                            onCheckForUpdates = {
                                scope.launch(Dispatchers.IO) {
                                    OtaUpdater.checkForUpdates(this@MainActivity, "1.0", true)
                                }
                            },
                            onBack = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}
