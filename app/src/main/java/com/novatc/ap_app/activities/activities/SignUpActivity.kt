package com.novatc.ap_app.activities.activities

import Firestore.Fireclass
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import model.BaseActivity
import model.User

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        super.hideStatusBar()
        FirebaseApp.initializeApp(this)
        btn_sign_up_sign_up.setOnClickListener {
            registerUser()
        }
        btn_sign_in_question.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActicity::class.java))
        }

    }

    fun userRegisteredSuccess() {
        Toast.makeText(this, "Successfully registered", Toast.LENGTH_LONG).show()
        FirebaseAuth.getInstance().signOut()
        finish()
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

                        val user = User(firebaseUser.uid, name, registeredMail)
                        Fireclass().registerUser(this@SignUpActivity, user)


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

}

