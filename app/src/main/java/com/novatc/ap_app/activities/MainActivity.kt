package com.novatc.ap_app.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.novatc.ap_app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)
        NavigationUI.setupWithNavController(bottomNav, navController)
        bottomNav.setOnItemSelectedListener{
            when (it.itemId) {
            R.id.fragment_pinboard -> {
                navController.navigate(R.id.fragment_pinboard)
                return@setOnItemSelectedListener true
            }
            R.id.fragment_rooms -> {
                navController.navigate(R.id.fragment_rooms)
                return@setOnItemSelectedListener true
            }
            R.id.fragment_events -> {
                navController.navigate(R.id.fragment_events)
                return@setOnItemSelectedListener true
            }
            R.id.fragment_profile -> {
                navController.navigate(R.id.fragment_profile)
                return@setOnItemSelectedListener true
            }
        }
            false
        }
        navController.addOnDestinationChangedListener{_, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.signUpFragment) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }
        Log.e("START", "NAV is here")

    }




}
