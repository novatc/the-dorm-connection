package com.novatc.ap_app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.PostAdapter
import com.novatc.ap_app.viewModels.PinboardViewModel
import kotlinx.android.synthetic.main.fragment_pinboard.view.*
import model.Post


class PinnboardFragment : Fragment() {
    private lateinit var layoutManager: LinearLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_pinboard, container, false)
        populatePostList(view)

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

    private fun populatePostList(view: View){
        val recyclerView: RecyclerView = view.rv_posts
        val model = ViewModelProvider(this)[PinboardViewModel::class.java]
        model.posts.observe(this, {posts ->
            posts.sortedByDescending {  it.date }
            recyclerView.adapter = PostAdapter(posts as ArrayList<Post>)
        })
        recyclerView.layoutManager = layoutManager
    }


}