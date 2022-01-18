package com.novatc.ap_app.fragments.post

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.PostAdapter
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.viewModels.you.MyPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_posts.view.*

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
        val recyclerView: RecyclerView = view.rv_my_posts
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
        val action = MyPostsFragmentDirections.actionMyPostsFragmentToPostDetailsFragment(post)
        findNavController().navigate(action)
    }


}