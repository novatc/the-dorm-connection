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
import com.google.firebase.auth.FirebaseUser
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import model.BaseActivity
import org.w3c.dom.Text

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        hideStatusBar()

        FirebaseApp.initializeApp(this)

        btn_sign_up_sign_up.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val name: String = et_sign_up_name.text.toString().trim { it <= ' ' }
        val mail: String = et_sign_up_email.text.toString().trim { it <= ' ' }
        val password: String = et_sign_up_password.text.toString().trim { it <= ' ' }

        if (validateForm(name, mail, password)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredMail = firebaseUser.email!!

                        Toast.makeText(
                            this@SignUpActivity, "$name hat sich erfolgreich mit " +
                                    "der $registeredMail registriert.", Toast.LENGTH_LONG
                        ).show()
                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(this, IntroActivity::class.java))

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@SignUpActivity,
                            "${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()


                    }
                }
        }

    }

    private fun validateForm(name: String, mail: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                Toast.makeText(this, "Bitte Namen angeben", Toast.LENGTH_LONG)
                false
            }
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