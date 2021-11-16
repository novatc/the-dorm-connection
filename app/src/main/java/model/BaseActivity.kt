package model

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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



}