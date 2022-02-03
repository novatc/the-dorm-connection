package com.novatc.ap_app.fragments

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.MainActivity
import com.novatc.ap_app.adapter.PostAdapter
import com.novatc.ap_app.viewModels.PinboardViewModel
import kotlinx.android.synthetic.main.fragment_pinboard.view.*
import com.novatc.ap_app.model.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PinnboardFragment : Fragment(), PostAdapter.OnItemClickListener {
    var postList: ArrayList<Post> = ArrayList()
    val model: PinboardViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pinboard, container, false)
        populatePostList(view)
        setAddPostButtonListener(view)
        return view
    }


    override fun onItemClick(position: Int) {
        val post = postList[position]
        val action = PinnboardFragmentDirections.actionFragmentPinboardToPostDetailsFragment(post)
        findNavController().navigate(action)
    }

    @ExperimentalCoroutinesApi
    private fun populatePostList(view: View) {
        view.pinboardListSpinner.visibility = View.VISIBLE
        val recyclerView: RecyclerView = view.rv_posts
        val model: PinboardViewModel by viewModels()
        model.postsList.observe(this, { posts ->


            postList = posts
            postList.sortedByDescending { it.date }
            view.pinboardListSpinner.visibility = View.GONE
            recyclerView.adapter = PostAdapter(postList, this)
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)


    }

    private fun setAddPostButtonListener(view: View) {
        val addPost: FloatingActionButton = view.btn_add_post
        addPost.setOnClickListener {
            val action = PinnboardFragmentDirections.actionFragmentPinboardToAddPostFragment()
            view.findNavController().navigate(action)
        }
    }


}