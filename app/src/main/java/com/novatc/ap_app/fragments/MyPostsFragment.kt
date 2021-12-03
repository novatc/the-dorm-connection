package com.novatc.ap_app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.PostAdapter
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.viewModels.MyPostViewModel
import com.novatc.ap_app.viewModels.PinboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_posts.view.*
import kotlinx.android.synthetic.main.fragment_pinboard.view.*
import kotlinx.android.synthetic.main.fragment_pinboard.view.rv_posts
import kotlinx.android.synthetic.main.fragment_profile_options.view.*

@AndroidEntryPoint
class MyPostsFragment : Fragment(), PostAdapter.OnItemClickListener {
    private lateinit var layoutManager: LinearLayoutManager
    var postList:ArrayList<Post> = ArrayList()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_my_posts, container, false)
        populateUSerPostList(view)


        return view
    }

    private fun populateUSerPostList(view: View){
        val recyclerView: RecyclerView = view.rv_posts
        val model = ViewModelProvider(this)[MyPostViewModel::class.java]
        model.posts.observe(this, {posts ->
            postList = posts
            postList.sortedByDescending {  it.date }
            recyclerView.adapter = PostAdapter(postList, this)
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onItemClick(position: Int) {
        val post = postList[position]
        val action = PinnboardFragmentDirections.actionFragmentPinboardToPostDetailsFragment(post)
        findNavController().navigate(action)
    }


}