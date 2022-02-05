package com.novatc.ap_app.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

/**
 * If there is no worker found, return null to use the default behaviour of [WorkManager]
 * (create worker using refection)
 *
 * In addition you can use dagger multi-binding to avoid manual check the workerClassName
 * but if you not familiar dagger multi-binding then it better to do it manually since
 * it easier to understand.
 *
 * Check out earlier commit to see the dagger multi-binding solution!
 *
 * @see WorkerFactory.createWorkerWithDefaultFallback
 */

class CustomFactory @Inject constructor(
    private val notification: Notification.Factory
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            Notification::class.java.name ->
                notification.create(appContext, workerParameters)
            else -> null
        }
    }
}