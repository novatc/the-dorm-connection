package com.novatc.ap_app.fragments.you

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.services.SwipeListener
import com.novatc.ap_app.viewModels.you.UpdateProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.view.*

@AndroidEntryPoint
class ProfileFragment : Fragment(), SwipeListener {

    private val model: UpdateProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setupOnUpdateListener(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var back1 = view.findNavController().backQueue
        print("test")
    }

    private fun setupOnUpdateListener(view: View) {
        view.profileUpdateButton.setOnClickListener {
            val profileName = view.profileName.text.trim().toString()
            val profileCurrentPassword = view.profileCurrentPassword.text.trim().toString()
            val profileNewPassword = view.profileNewPassword.text.trim().toString()
            val profilePasswordRepeat = view.profileRepeatPassword.text.trim().toString()

            if (profileName.isEmpty() && (profileCurrentPassword.isEmpty() ||
                        profileNewPassword.isEmpty() ||
                        profilePasswordRepeat.isEmpty())
            ) {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, R.string.empty_all_field_error, Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
            } else if (profileName.isNotEmpty() && profileCurrentPassword.isEmpty() &&
                profileNewPassword.isEmpty() &&
                profilePasswordRepeat.isEmpty()
            ) {
                updateUserName(profileName)
            } else if (profileName.isEmpty() && profileCurrentPassword.isNotEmpty() &&
                profileNewPassword.isNotEmpty() &&
                profilePasswordRepeat.isNotEmpty()
            ) {
                updatePassword(profileCurrentPassword, profileNewPassword, profilePasswordRepeat)
            } else if (profileName.isNotEmpty() && profileCurrentPassword.isNotEmpty() &&
                profileNewPassword.isNotEmpty() &&
                profilePasswordRepeat.isNotEmpty()
            ) {
                updateUserName(profileName)
                updatePassword(profileCurrentPassword, profileNewPassword, profilePasswordRepeat)
            }
        }
    }

    private fun updateUserName(userName: String) {
        model.updateName(userName)
        model.updateNameRequest.observe(this, { request ->
            if (request.status == Request.Status.SUCCESS) {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.profile_update_name_success, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFragmentProfile())
            } else {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,request.message!!, Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
            }
        })
    }

    private fun updatePassword(
        currentPassword: String,
        newPassword: String,
        repeatPassword: String
    ) {
        if (newPassword != repeatPassword) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView, R.string.profile_update_password_match_error, Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
        }
        model.updatePassword(currentPassword, newPassword)
        model.updatePasswordRequest.observe(this, { request ->
            if (request.status == Request.Status.SUCCESS) {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, R.string.profile_update_password_succeess, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToFragmentProfile())
            } else {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, request.message!!, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
            }
        })
    }

    override fun onSwipeLeft(view: View) {
        TODO("Not yet implemented")
    }

    override fun onSwipeRight(view: View) {
        TODO("Not yet implemented")
    }
}