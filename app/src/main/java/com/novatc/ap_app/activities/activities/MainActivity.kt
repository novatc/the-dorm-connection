package com.novatc.ap_app.activities.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import model.BaseActivity

class MainActivity : BaseActivity() {
    private val userProfile = ProfileFragment()
    private val events = EventFragment()
    private val pinBoard = PinnboardFragment()
    private val rooms = RoomFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        super.hideStatusBar()
        replaceFragments(pinBoard)


        bottomNav.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.menu_rooms ->{
                    replaceFragments(rooms)
                    true
                }
                R.id.menu_events ->{
                    replaceFragments(events)
                    true
                }
                R.id.menu_pinboard ->{
                    replaceFragments(pinBoard)
                    true
                }
                R.id.menu_profile ->{
                    replaceFragments(userProfile)
                    true
                }


                else -> true
            }

        }

    }

    private fun replaceFragments(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


}
