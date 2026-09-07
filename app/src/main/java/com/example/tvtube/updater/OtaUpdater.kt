package com.example.tvtube.updater

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class GitHubRelease(
    @SerialName("tag_name") val tagName: String,
    val assets: List<GitHubAsset>
)

@Serializable
data class GitHubAsset(
    @SerialName("browser_download_url") val downloadUrl: String,
    val name: String
)

object OtaUpdater {
    private const val CHANNEL_ID = "ota_updates"
    private const val NOTIFICATION_ID = 1001
    private const val GITHUB_REPO = "jch0029987-glitch/tvtube"

    suspend fun checkForUpdates(context: Context, currentVersion: String, manualCheck: Boolean = false) {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        try {
            val response: GitHubRelease = client.get("https://api.github.com/repos/$GITHUB_REPO/releases/latest").body()
            if (response.tagName != currentVersion) {
                val apkAsset = response.assets.firstOrNull { it.name.endsWith(".apk") }
                if (apkAsset != null) {
                    downloadAndInstall(context, apkAsset.downloadUrl)
                }
            }
        } catch (_: Exception) {
        } finally {
            client.close()
        }
    }

    private suspend fun downloadAndInstall(context: Context, apkUrl: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setTitle("TVTube Update Available")
            .setContentText("Downloading update...")
            .setOngoing(true)
            .setProgress(0, 0, true)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        val client = HttpClient()
        try {
            val response: HttpResponse = client.get(apkUrl)
            val apkFile = File(context.cacheDir, "update.apk")
            apkFile.writeBytes(response.readBytes())

            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val completeBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setTitle("Update Ready")
                .setContentText("Click to install TVTube")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(false)

            notificationManager.notify(NOTIFICATION_ID, completeBuilder.build())
            context.startActivity(intent)
        } finally {
            client.close()
        }
    }

    private fun createNotificationChannel(manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "App Updates", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }
    }
}
