package com.novatc.ap_app.fragments

import Firestore.Fireclass
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.SignUpActivity
import com.novatc.ap_app.viewModels.ProfileViewModel
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
            view.tv_user_name.setText(it.username)
        })

        view.btn_my_posts.setOnClickListener {
            val myPosts = MyPostsFragment()
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment, myPosts)
            }
        }

        view.btn_edit_profile.setOnClickListener {
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment, profile)
            }
        }
        view.btn_edit_wg.setOnClickListener {
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment, wg)
            }
        }
        view.btn_delet_profile.setOnClickListener {
            Fireclass().deleteUser()
            startActivity(Intent(requireActivity(), SignUpActivity::class.java))
            Toast.makeText(requireActivity(), "Your account hast been deleted.", Toast.LENGTH_SHORT)
                .show()
        }
        view.btn_profile_logout.setOnClickListener {
            Fireclass().logout()
            startActivity(Intent(requireActivity(), SignUpActivity::class.java))
        }
        view.btn_select_dorm.setOnClickListener {
            val dorm = ChooseDormFragment()
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment, dorm)
            }
        }
        return view
    }

    private suspend fun getUserInfo() {
        try {
            val user = Fireclass().getUserData(Fireclass().getCurrentUserID())
        } catch (e: Exception) {
            Log.d("FIRE", e.toString()) //Don't ignore potential errors!
        }
    }

}