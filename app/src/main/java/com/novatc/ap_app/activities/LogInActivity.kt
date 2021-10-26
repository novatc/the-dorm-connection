package com.novatc.ap_app.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import model.BaseActivity

class LogInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        hideStatusBar()
        FirebaseApp.initializeApp(this)
        btn_sign_in.setOnClickListener { signInValidUser() }

    }
    private fun signInValidUser(){
        val mail: String = et_sign_in_mail.text.toString().trim { it <= ' ' }
        val password: String = et_sign_in_password.text.toString().trim { it <= ' ' }
        if (validateForm(mail, password)){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(
                        this@LogInActivity,
                        "Erfolgreich angemeldet",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                }else{
                    Toast.makeText(
                        this@LogInActivity,
                        "${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun validateForm(mail: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(mail) -> {
                Toast.makeText(this, "Bitte Mail angeben", Toast.LENGTH_LONG)
                false
            }
            TextUtils.isEmpty(mail) -> {
                Toast.makeText(this, "Bitte Passwort angeben", Toast.LENGTH_LONG)
                false
            }
            else -> {
                true
            }
        }
    }

    fun hideStatusBar() {
        //if Andorid version is too old, use deprecated features to get the same result as with a
        //modern skd
        if (Build.VERSION.SDK_INT < 30) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            window.setDecorFitsSystemWindows(false)
            val controler = window.insetsController
            if (controler != null) {
                controler.hide(WindowInsets.Type.statusBars())
                controler.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
            }
        }
    }
}