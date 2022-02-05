package com.novatc.ap_app.fragments.pinboard

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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        val postAdapter = PostAdapter(this)
        recyclerView.adapter = postAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        model.posts.observe(this, { posts ->
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val result = posts.sortedByDescending {
                LocalDate.parse(it.date, dateTimeFormatter)
            }
            postAdapter.differ.submitList(result)
            postList = posts

        })
    }

    override fun onItemClick(position: Int) {
        val post = postList[position]
        val action = MyPostsFragmentDirections.actionMyPostsFragmentToPostDetailsFragment(post)
        findNavController().navigate(action)
    }


}