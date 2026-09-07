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
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.io.File

object OtaUpdater {
    private const val CHANNEL_ID = "ota_updates"
    private const val NOTIFICATION_ID = 1001

    suspend fun downloadAndInstall(context: Context, apkUrl: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        // Show starting notification
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setTitle("TVTube Update")
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
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Update notification to ready state
            val completeBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setTitle("TVTube Update Ready")
                .setContentText("Tap to install the latest version")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(false)

            notificationManager.notify(NOTIFICATION_ID, completeBuilder.build())

            // Automatically launch the installer prompt
            context.startActivity(intent)

        } catch (e: Exception) {
            val errorBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .setTitle("TVTube Update Failed")
                .setContentText("Could not download update file.")
                .setAutoCancel(true)

            notificationManager.notify(NOTIFICATION_ID, errorBuilder.build())
        } finally {
            client.close()
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for TVTube app updates"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
