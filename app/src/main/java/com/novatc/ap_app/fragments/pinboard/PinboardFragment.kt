package com.novatc.ap_app.fragments

import android.icu.text.SimpleDateFormat
import com.novatc.ap_app.services.SwipeGestureListener
import com.novatc.ap_app.services.SwipeListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PinboardFragment : Fragment(), PostAdapter.OnItemClickListener, SwipeListener {
    var postList: List<Post> = emptyList()
    val model: PinboardViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pinboard, container, false)
        view.posts_constraintLayout.setOnTouchListener(SwipeGestureListener(this))
        populatePostList(view)
        setAddPostButtonListener(view)
        return view
    }


    override fun onItemClick(position: Int) {
        val post = postList[position]
        val action = PinboardFragmentDirections.actionFragmentPinboardToPostDetailsFragment(post)
        findNavController().navigate(action)
    }

    @ExperimentalCoroutinesApi
    private fun populatePostList(view: View) {
        view.pinboardListSpinner.visibility = View.VISIBLE
        val recyclerView: RecyclerView = view.rv_posts
        val model: PinboardViewModel by viewModels()

        val postAdapter = PostAdapter(this)
        recyclerView.adapter = postAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        model.postList.observe(viewLifecycleOwner) { posts ->
            view.pinboardListSpinner.visibility = View.GONE
            val result = posts.sortedByDescending {
                it.date
            }
            postAdapter.differ.submitList(result)
            postList = result
            val data: Data = workDataOf("SIZE_OF_POSTLIST" to postList.size)
            val backgroundService =
                PeriodicWorkRequestBuilder<Notification>(1, TimeUnit.MINUTES).setInputData(data)
                    .build()
            WorkManager.getInstance(requireContext()).enqueue(backgroundService)

        }

    }

    private fun setAddPostButtonListener(view: View) {
        val addPost: FloatingActionButton = view.btn_add_post
        addPost.setOnClickListener {
            val action = PinboardFragmentDirections.actionFragmentPinboardToAddPostFragment()
            view.findNavController().navigate(action)
        }

    }


    override fun onSwipeLeft(view: View) {
        val action = PinboardFragmentDirections.actionFragmentPinboardToFragmentRooms()
        view.findNavController().navigate(action)
    }

    override fun onSwipeRight(view: View) {
    }

}