package com.novatc.ap_app

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideStatusBar()

    }

    fun hideStatusBar(){
        //if Andorid version is too old, use deprecated features to get the same result as with a
        //modern skd
        if(Build.VERSION.SDK_INT < 30) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            }else {
                window.setDecorFitsSystemWindows(false)
            val controler = window.insetsController
            if(controler != null){
                controler.hide(WindowInsets.Type.statusBars())
                controler.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
            }
        }
    }
}