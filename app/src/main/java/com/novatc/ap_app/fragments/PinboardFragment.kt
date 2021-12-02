package com.novatc.ap_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.PostAdapter
import com.novatc.ap_app.viewModels.PinboardViewModel
import kotlinx.android.synthetic.main.fragment_pinboard.view.*
import com.novatc.ap_app.model.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.view.*

@AndroidEntryPoint
class PinnboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_pinboard, container, false)
        populatePostList(view)
        setAddPostButtonListener(view)
        return view
    }

    private fun populatePostList(view: View){
        val recyclerView: RecyclerView = view.rv_posts
        val model = ViewModelProvider(this)[PinboardViewModel::class.java]
        model.posts.observe(this, {posts ->
            posts.sortedByDescending {  it.date }
            recyclerView.adapter = PostAdapter(posts as ArrayList<Post>)
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