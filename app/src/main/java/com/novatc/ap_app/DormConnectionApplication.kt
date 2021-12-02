package com.novatc.ap_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Seems neccessary when you want to use Hilt dependency injection
@HiltAndroidApp
class DormConnectionApplication: Application() {

}