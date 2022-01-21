package com.novatc.ap_app.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.MainActivity

class FirebaseMessaging : FirebaseMessagingService() {

    private val CHANNEL_NAME = "THE DORM NOTIFICATION CHANNEL"
    private val notificationId = "42"
    private val NOTIFICATION_ID = 101

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        createNotificationChannel()
        remoteMessage.notification!!.title?.let {
            remoteMessage.notification!!.body?.let { it1 ->
                createNotification(
                    it,
                    it1
                )
            }
        }

    }

    private fun createNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        val builder = NotificationCompat.Builder(this, CHANNEL_NAME)
            .setSmallIcon(R.drawable.transfer_64px)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())

        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "THE DORM"
            val descriptionText = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notificationId, CHANNEL_NAME, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemovedView(title: String, message: String): RemoteViews? {
        val remoteView = RemoteViews("com.novatc.ap_app.services", R.layout.notification)
        remoteView.setTextViewText(R.id.new_post_notification_title, title)
        remoteView.setTextViewText(R.id.new_post_notification_description, message)
        remoteView.setImageViewResource(R.id.new_post_notification, R.drawable.transfer_64px)
        return remoteView
    }

}
