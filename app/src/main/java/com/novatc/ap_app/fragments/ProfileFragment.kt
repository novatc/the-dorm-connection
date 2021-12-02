package com.novatc.ap_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.novatc.ap_app.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.view.*

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        view.btn_edit_profile_safe.setOnClickListener {
            val profileOptions = ProfileOptionsFragment()
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment, profileOptions)
            }
        }
        return view
    }
}