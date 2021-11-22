package com.novatc.ap_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.novatc.ap_app.R
import com.novatc.ap_app.viewModels.PinboardViewModel
import kotlinx.android.synthetic.main.fragment_pinboard.view.*


class PinnboardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_pinboard, container, false)
        val viewModel = ViewModelProvider(this)[PinboardViewModel::class.java]
        val postList = viewModel.posts.observe(viewLifecycleOwner, Observer {
            Log.e("POST PINBOARD", it.toString())
        })

        view.btn_add_post.setOnClickListener {
            val newPost = AddPostFragment()
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.fragment_container, newPost)
            }
        }
        return view
    }


}