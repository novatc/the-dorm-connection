package com.novatc.ap_app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.novatc.ap_app.R
import model.BaseActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        super.hideStatusBar()

        // Changes the activity to the intro activity after 2.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, SignInActicity::class.java))
            finish()
        }, 1500)

    }


}