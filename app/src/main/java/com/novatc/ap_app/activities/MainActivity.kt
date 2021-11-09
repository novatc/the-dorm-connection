package com.novatc.ap_app.activities

import android.os.Bundle
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.activity_main.*
import model.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.hideStatusBar()

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    setContent("Rooms")
                    true
                }
                R.id.menu_notification -> {
                    setContent("Events")
                    true
                }
                R.id.menu_search -> {
                    setContent("Pinboard")
                    true
                }
                R.id.menu_profile -> {
                    setContent("Profile")
                    true
                }
                else -> false
            }
        }
    }

    private fun setContent(content: String) {
        setTitle(content)
        tvLabel.text = content
    }
}
