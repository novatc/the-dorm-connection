package com.novatc.ap_app.activities.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import model.BaseActivity

class UserProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_user_profile)
        super.hideStatusBar()

        btn_edit_profile.setOnClickListener{
            startActivity(Intent(this@UserProfileActivity, EditProfileActivity::class.java))
        }
        btn_edit_wg.setOnClickListener{
            startActivity(Intent(this@UserProfileActivity, EditWGActivity::class.java))
        }
        btn_delet_profile.setOnClickListener{
            Toast.makeText(this, "Your Profile has been deleted", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@UserProfileActivity, SignUpActivity::class.java))
        }

    }
}