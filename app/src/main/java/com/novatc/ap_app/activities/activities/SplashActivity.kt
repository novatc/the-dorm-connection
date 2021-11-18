package com.novatc.ap_app.activities.activities

import Firestore.Fireclass
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.R
import model.BaseActivity

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        super.hideStatusBar()

        // Changes the activity to the intro activity after 2.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val user = Firebase.auth.currentUser
            if (user == null) {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java))

            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }

            finish()
        }, 1500)

    }


}