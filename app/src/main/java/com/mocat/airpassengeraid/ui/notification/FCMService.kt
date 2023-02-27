package com.mocat.airpassengeraid.ui.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FCMService : FirebaseMessagingService() {


  /*  override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if the message contains data
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Handle data message
            if (remoteMessage.data.containsKey("title") && remoteMessage.data.containsKey("message")) {
                val title = remoteMessage.data["title"]
                val message = remoteMessage.data["message"]
                Log.d(TAG, "Title: $title, Message: $message")

                // TODO: Handle the data message
            }
        }

        // Check if the message contains a notification
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // Handle notification message
        }
    }*/

    //function for received Notification in App Online and Offline
    @SuppressLint("NewApi")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification!!.title
        val text = remoteMessage.notification!!.body
        val CHANNEL_ID = "MESSAGE"
        val channel = NotificationChannel(CHANNEL_ID, "Message Notification", NotificationManager.IMPORTANCE_HIGH)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(com.mocat.airpassengeraid.R.drawable.app_icon_round)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
        NotificationManagerCompat.from(this).notify(1, notification.build())
        super.onMessageReceived(remoteMessage)
        //setVibrate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun setVibrate() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun createNotification(channelId: String, title: String, body: String, pendingIntent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(com.mocat.airpassengeraid.R.drawable.app_icon_round)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setOnlyAlertOnce(true)
            color = ContextCompat.getColor(this@FCMService, com.mocat.airpassengeraid.R.color.app_theme)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(pendingIntent)
        }
    }

    override fun onNewToken(token: String) {
        //Log.d(TAG, "Refreshed token: $token")
        // Send the new FCM token to the server.
    }

}