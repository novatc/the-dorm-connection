package com.novatc.ap_app

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        hideStatusBar()
        btn_intro_log_in.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }
        btn_intro_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

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