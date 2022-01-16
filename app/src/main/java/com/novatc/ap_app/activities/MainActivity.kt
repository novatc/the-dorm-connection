package com.novatc.ap_app.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_main)
        }
        catch (e: Exception){
            Log.e(TAG, "onCreateView", e);
            throw e;
        }
        super.hideStatusBar()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)
        NavigationUI.setupWithNavController(bottomNav, navController)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.signUpFragment) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }

    }




}
