package com.novatc.ap_app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import com.novatc.ap_app.model.BaseActivity

class MainActivity : BaseActivity() {
    private val userProfile = ProfileOptionsFragment()
    private val events = EventFragment()
    private val pinBoard = PinnboardFragment()
    private val rooms = RoomFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.hideStatusBar()
        if (savedInstanceState == null) {
            super.replaceFragments(pinBoard)
        }
        bottomNav.menu.getItem(0).isCheckable = true
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_rooms -> {
                    super.replaceFragments(rooms)
                    true
                }
                R.id.menu_events -> {
                    super.replaceFragments(events)
                    true
                }
                R.id.menu_pinboard -> {
                    super.replaceFragments(pinBoard)
                    true
                }
                R.id.menu_profile -> {
                    super.replaceFragments(userProfile)
                    true
                }
                else -> true
            }
        }
    }




}
