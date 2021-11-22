package com.novatc.ap_app.fragments

import Firestore.Fireclass
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_add_post.view.*
import kotlinx.android.synthetic.main.fragment_add_post.view.et_post_keywords
import kotlinx.android.synthetic.main.fragment_add_post.view.et_post_text
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import model.Post

class AddPostFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
        view.btn_safe_new_post.setOnClickListener {
            val pinBoard = PinnboardFragment()
            if (view.et_post_headline.text.isEmpty()) {
                Toast.makeText(requireContext(), "Pls set a post headline", Toast.LENGTH_SHORT)
                    .show()
            }
            if (view.et_post_text.text.isEmpty()) {
                Toast.makeText(requireContext(), "Pls set a post text", Toast.LENGTH_SHORT).show()
            }
            if (view.et_post_keywords.text.isEmpty()) {
                Toast.makeText(requireContext(), "Pls set a post keyword", Toast.LENGTH_SHORT)
                    .show()
            }
            if (view.et_post_headline.text.isNotEmpty() && view.et_post_text.text.isNotEmpty() && view.et_post_keywords.text.isNotEmpty()) {
                val post: Post = Post(headline = view.et_post_headline.text.toString(),
                    text = view.et_post_text.text.toString(),
                    keyword = view.et_post_keywords.text.toString(),
                    creator = Fireclass().getCurrentUserID()
                )
                Fireclass().addPost(post)
                Toast.makeText(requireContext(), "Post created", Toast.LENGTH_SHORT).show()
                parentFragmentManager.commit {
                    isAddToBackStackAllowed
                    setReorderingAllowed(true)
                    replace(R.id.fragment_container, pinBoard)
                }
            }

        }
        return view

    }

}