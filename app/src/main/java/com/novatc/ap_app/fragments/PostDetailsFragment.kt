package com.novatc.ap_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.novatc.ap_app.R
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import com.novatc.ap_app.viewModels.DetailedPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_details.view.*
import kotlinx.android.synthetic.main.post_list_item.view.*
import kotlinx.android.synthetic.main.post_list_item.view.tv_post_text
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailsFragment : Fragment() {
    private val args by navArgs<PostDetailsFragmentArgs>()
    @Inject
    lateinit var  userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_post_details, container, false)
        val post:Post = args.clickedPost
        Log.e("POST", "Post clicked: ${post}")

        view.tv_detail_post_title.text = post.headline
        view.tv_detail_post_date.text = post.date
        view.tv_detail_post_author.text = post.creator
        view.tv_detail_post_text.text = post.text
        view.tv_detail_post_keywords.text = post.keyword

        if (post.creatorID == userRepository.readCurrentId()){
            view.btn_delete_post.visibility = View.VISIBLE
        }else{
            view.btn_delete_post.visibility = View.GONE
        }

        return view
    }

}