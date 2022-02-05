package com.novatc.ap_app.services
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.novatc.ap_app.R
import com.novatc.ap_app.repository.PostRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@HiltWorker
class Notification @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val postRepository: PostRepository
) :
    CoroutineWorker(appContext, workerParams) {
    @AssistedFactory
    interface Factory {
        fun create(appContext: Context, params: WorkerParameters): Notification
    }

    override suspend fun doWork(): Result {
        val startSize = inputData.getInt("SIZE_OF_POSTLIST", 0)
        val newSize = postRepository.getPosts().size
        sendNotification(
            "THE DORM CONNECTION", "A new post is available! old :$startSize" +
                    "new: $newSize"
        )
        if (newSize > startSize) {
            sendNotification(
                "THE DORM CONNECTION", "A new post is available! old :$startSize" +
                        "new: $newSize"
            )
        }
        Log.e("SERVICE", "RUNNING...")
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //If on Oreo then notification required a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("NewPost", "New Post", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            "NewPost"
        )
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.transfer_64px)
        notificationManager.notify(1, notification.build())
    }

}






