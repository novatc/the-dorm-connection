package com.novatc.ap_app.fragments

import com.novatc.ap_app.firestore.UserFirestore
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.viewModels.LoginViewModel
import com.novatc.ap_app.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_options.view.*

@AndroidEntryPoint
class ProfileOptionsFragment : Fragment() {

    val model: ProfileViewModel by viewModels()
    private var userWgId = "";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_options, container, false)

        model.userProfile.observe(viewLifecycleOwner, Observer {
            view.tv_user_name.text = it.username
            view.tv_user_dorm.text = it.userDorm
            userWgId = it.userWgId
            if (it.userDorm!=""){
                view.btn_select_dorm.isEnabled = false
                view.btn_select_dorm.alpha = 0.5F
            }
        })

        setMyPostButtonListener(view)
        setEditProfileButtonListener(view)
        setEditWGButtonListener(view)
        setDeleteUserButtonListener(view)
        setLogOutUserButtonListener(view)
        setEditDormButtonListener(view)

        return view
    }

    private fun setMyPostButtonListener(view: View) {
        val myPostButton: Button = view.btn_my_posts
        myPostButton.setOnClickListener {
            val action = ProfileOptionsFragmentDirections.actionFragmentProfileToMyPostsFragment()
            view.findNavController().navigate(action)
        }
    }

    private fun setEditProfileButtonListener(view: View) {
        val editProfileButton: Button = view.btn_edit_profile
        editProfileButton.setOnClickListener {
            val action = ProfileOptionsFragmentDirections.actionFragmentProfileToProfileFragment3()
            view.findNavController().navigate(action)
        }
    }

    private fun setEditWGButtonListener(view: View) {
        val editWGButton: Button = view.btn_edit_wg
        editWGButton.setOnClickListener {
            if (userWgId != "") {
                view.findNavController().navigate(ProfileOptionsFragmentDirections.actionFragmentProfileToWgDetailFragment(userWgId))
            } else {
                view.findNavController().navigate(ProfileOptionsFragmentDirections.actionFragmentProfileToWgChoose())
            }

        }
    }

    private fun setEditDormButtonListener(view: View) {
        val editDormButton: Button = view.btn_select_dorm
        editDormButton.setOnClickListener {
            val action =
                ProfileOptionsFragmentDirections.actionFragmentProfileToChooseDormFragment()
            view.findNavController().navigate(action)
        }
    }

    private fun setDeleteUserButtonListener(view: View) {
        val deleteProfileButton: Button = view.btn_delet_profile
        deleteProfileButton.setOnClickListener {
            model.deleteUser()
            model.deleteRequest.observe(this) { request ->
                when (request.status) {

                    Request.Status.SUCCESS -> {
                        Toast.makeText(activity, R.string.delete_user_success, Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(
                            ProfileOptionsFragmentDirections.actionFragmentProfileToSignUpFragment()
                        )
                    }
                    else -> Toast.makeText(activity, request.message!!, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setLogOutUserButtonListener(view: View) {
        val setLogOutUserButton: Button = view.btn_profile_logout
        setLogOutUserButton.setOnClickListener {
            UserFirestore().logout()
            val action = ProfileOptionsFragmentDirections.actionFragmentProfileToLoginFragment()
            view.findNavController().navigate(action)
        }
    }

}