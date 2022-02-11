package com.novatc.ap_app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import androidx.work.Configuration

// Seems necessary when you want to use Hilt dependency injection
@HiltAndroidApp
class DormConnectionApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


}