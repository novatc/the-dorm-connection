package com.novatc.ap_app.fragments.post

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
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import com.novatc.ap_app.viewModels.PostDetailsViewModel
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_details.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {
    private val args by navArgs<PostDetailsFragmentArgs>()
    private val postDetailsViewModel: PostDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_post_details, container, false)
        val post:Post = args.clickedPost
        Log.e("FIRE", "Post viewed: ${post}")

        view.tv_detail_post_title.text = post.headline
        view.tv_detail_post_date.text = post.date
        view.tv_detail_post_author.text = post.creator
        view.tv_detail_post_text.text = post.text
        view.tv_detail_post_keywords.text = post.keyword

        if (post.creatorID == postDetailsViewModel.readCurrentID()){
            view.btn_delete_post.visibility = View.VISIBLE
            view.btn_delete_post.setOnClickListener {
                lifecycleScope.launch {
                    post.key?.let { it1 -> postDetailsViewModel.deletePost(postID = it1) }
                }
                val action = PostDetailsFragmentDirections.actionPostDetailsFragmentToFragmentPinboard()
                findNavController().navigate(action)
            }
        }else{
            view.btn_delete_post.visibility = View.GONE
        }

        return view
    }

}