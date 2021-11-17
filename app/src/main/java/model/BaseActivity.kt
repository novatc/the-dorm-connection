package model

import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.novatc.ap_app.R

open class BaseActivity: AppCompatActivity() {
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

    fun replaceFragments(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
        }
    }

    fun validateForm(name:String, mail: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(mail) -> {
                Toast.makeText(this, "Bitte Mail angeben", Toast.LENGTH_LONG)
                false
            }
            TextUtils.isEmpty(mail) -> {
                Toast.makeText(this, "Bitte Passwort angeben", Toast.LENGTH_LONG)
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Bitte Mail angeben", Toast.LENGTH_LONG)
                false
            }
            else -> {
                true
            }
        }
    }

    fun validateForm(name: String, mail: String): Boolean {
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


}