package com.novatc.ap_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.PostAdapter
import com.novatc.ap_app.viewModels.pinboard.PinboardViewModel
import kotlinx.android.synthetic.main.fragment_pinboard.view.*
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.services.Notification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

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
        val worker: WorkRequest = PeriodicWorkRequestBuilder<Notification>(1,TimeUnit.MINUTES).build()
        WorkManager.getInstance(requireContext()).enqueue(worker)
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
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            posts.sortedByDescending {
                LocalDate.parse(it.date, dateTimeFormatter)
            }
            postList = posts
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