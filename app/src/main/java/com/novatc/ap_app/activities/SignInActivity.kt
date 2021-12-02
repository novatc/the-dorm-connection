package com.novatc.ap_app.activities

import com.novatc.ap_app.firestore.UserFirestore
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.novatc.ap_app.model.BaseActivity
import com.novatc.ap_app.model.User

class SignInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        super.hideStatusBar()
        FirebaseApp.initializeApp(this)
        btn_sign_in.setOnClickListener { signInValidUser() }
        btn_sign_up_question.setOnClickListener{
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

    }
    private fun signInValidUser(){
        val mail: String = et_sign_in_mail.text.toString().trim { it <= ' ' }
        val password: String = et_sign_in_password.text.toString().trim { it <= ' ' }
        if (super.validateForm(mail, password)){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password).addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    UserFirestore().signInUser(this)
                }else{
                    Toast.makeText(
                        this@SignInActivity,
                        "${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun signInSuccess(loggedInUse: User) {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }



}