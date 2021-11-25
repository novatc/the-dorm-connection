package com.novatc.ap_app.activities

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import model.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//        super.hideStatusBar()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)
        NavigationUI.setupWithNavController(bottomNav, navController)


    }




}
