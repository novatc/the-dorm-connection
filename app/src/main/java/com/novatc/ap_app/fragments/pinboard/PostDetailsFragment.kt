package com.novatc.ap_app.fragments.pinboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.CommentAdapter
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.model.User
import com.novatc.ap_app.viewModels.pinboard.PostDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_details.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PostDetailsFragment : Fragment(), CommentAdapter.OnItemClickListener {
    private val args by navArgs<PostDetailsFragmentArgs>()
    private val postDetailsViewModel: PostDetailsViewModel by viewModels()
    private var commentListOnPost: List<Comment> = emptyList()
    private var currentUser: User = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_details, container, false)
        val post: Post = args.clickedPost
        postDetailsViewModel.setPost(post)
        postDetailsViewModel.loadComments()
        populateCommentsList(view, post)

        view.tv_detail_post_title.text = post.headline
        view.tv_detail_post_date.text = post.date?.let { convertTime(it) }
        view.tv_detail_post_author.text = post.creator
        view.tv_detail_post_text.text = post.text
        view.tv_detail_post_keywords.text = post.keyword

        checkOwnership(view, post)



        view.btn_send_comment.setOnClickListener {
            sendComment(view, post)

        }

        return view
    }

    override fun onItemClick(position: Int) {
        val comment = commentListOnPost[position]
        lifecycleScope.launch {
            args.clickedPost.id?.let {
                postDetailsViewModel.deleteComment(
                    commentID = comment.id,
                    it
                )
            }
        }
    }

    private fun sendComment(view: View, post: Post) {
        if (view.et_write_comment.text.isNotEmpty()) {
            val c = Comment(
                authorID = currentUser.id,
                content = view.et_write_comment.text.toString(),
                authorName = currentUser.username,
                date = getCurrentDate()
            )

            lifecycleScope.launch {
                post.id?.let { it1 ->
                    postDetailsViewModel.addComment(post.id!!, c)
                }
            }

        }
    }

    private fun checkOwnership(view: View, post: Post) {
        if (post.creatorID == postDetailsViewModel.readCurrentID()) {
            view.btn_delete_post.visibility = View.VISIBLE
            view.btn_delete_post.setOnClickListener {
                lifecycleScope.launch {
                    post.id?.let { it1 -> postDetailsViewModel.deletePost(postID = it1) }
                }
                val action =
                    PostDetailsFragmentDirections.actionPostDetailsFragmentToFragmentPinboard()
                findNavController().navigate(action)
            }
        } else {
            view.btn_delete_post.visibility = View.GONE
        }
    }

    @ExperimentalCoroutinesApi
    private fun populateCommentsList(view: View, post: Post) {
        val recyclerView: RecyclerView = view.rv_comments_on_post
        val model: PostDetailsViewModel by viewModels()

        postDetailsViewModel.setPost(post)

        postDetailsViewModel.userProfile.observe(viewLifecycleOwner) {
            currentUser = it

            val commentAdapter = CommentAdapter(currentUser.id, this)
            recyclerView.adapter = commentAdapter
            recyclerView.layoutManager = LinearLayoutManager(activity)

            model.comments.observe(viewLifecycleOwner) { comment ->
                val result = comment.sortedByDescending { it.date }
                commentAdapter.differ.submitList(result)
                commentListOnPost = result
            }

        }
    }

    private fun convertTime(unixTimestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm")
        val date = java.util.Date(unixTimestamp)
        return sdf.format(date)
    }

    private fun getCurrentDate(): Long {
        return System.currentTimeMillis()
    }


}