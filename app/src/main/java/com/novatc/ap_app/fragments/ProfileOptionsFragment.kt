package com.novatc.ap_app.fragments

import com.novatc.ap_app.Firestore.Fireclass
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.SignUpActivity
import com.novatc.ap_app.viewModels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_add_post.view.*
import kotlinx.android.synthetic.main.fragment_profile_options.view.*

class ProfileOptionsFragment : Fragment() {
    private val profile = ProfileFragment()
    private val wg = EditWgFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_options, container, false)
        val viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.userProfile.observe(viewLifecycleOwner, Observer {
            view.tv_user_name.text = it.username
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
            val action = ProfileOptionsFragmentDirections.actionFragmentProfileToEditWgFragment()
            view.findNavController().navigate(action)
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
            Fireclass().deleteUser()
            Toast.makeText(requireActivity(), "Your account hast been deleted.", Toast.LENGTH_SHORT)
                .show()
            val action = ProfileOptionsFragmentDirections.actionFragmentProfileToSignUpActivity()
            view.findNavController().navigate(action)
        }
    }

    private fun setLogOutUserButtonListener(view: View) {
        val setLogOutUserButton: Button = view.btn_profile_logout
        setLogOutUserButton.setOnClickListener {
            Fireclass().logout()
            val action = ProfileOptionsFragmentDirections.actionFragmentProfileToSignInActivity()
            view.findNavController().navigate(action)
        }
    }


}