package com.novatc.ap_app.fragments.wg

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.viewModels.wg.CreateWgViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wg_create.view.*

@AndroidEntryPoint
class WgCreateFragment : Fragment() {

    private val model: CreateWgViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wg_create, container, false)
        setupOnCreateListener(view)
        return view
    }

    fun setupOnCreateListener(view: View) {
        view.wgCreateButton.setOnClickListener {
            val name =  view.wgCreateName.text.toString().trim()
            val slogan = view.wgCreateSlogan.text.toString().trim()
            if (!isFormValid(name, slogan)) return@setOnClickListener
            model.createWg(name, slogan)
            model.createEventRequest.observe(this, {request ->
                if (request.status == Request.Status.SUCCESS) {
                    val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                    Snackbar.make(bottomNavView, R.string.wg_create_success, Snackbar.LENGTH_SHORT).apply {
                        anchorView = bottomNavView
                    }.show()
                    view.findNavController().navigate(WgCreateFragmentDirections.actionWgCreateFragmentToWgFragment())
                } else {
                    val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                    Snackbar.make(bottomNavView, request.message!!, Snackbar.LENGTH_LONG).apply {
                        anchorView = bottomNavView
                    }.show()
                }
            })
        }
    }

    private fun isFormValid(name: String, slogan: String): Boolean {
        if (name.isBlank() || slogan.isBlank()) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView, R.string.empty_field_error, Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        return true
    }

}