package com.novatc.ap_app.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.MainActivity
import com.novatc.ap_app.firestore.PostFirestore
import com.novatc.ap_app.repository.PostRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class Notification @Inject constructor(
    private val postRepository: PostRepository,
    appContext: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        Log.e("SERVICE", "RUNNING...")
        sendNotification("Service", "Hello from the Background!")
        val oldPosts = postRepository.getPosts().size
        return Result.success()


    }


    private fun sendNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //If on Oreo then notification required a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            "default"
        )
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.transfer_64px)
        notificationManager.notify(1, notification.build())
    }

}



