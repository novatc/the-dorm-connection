package com.novatc.ap_app.fragments.pinboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_add_post.view.*
import kotlinx.android.synthetic.main.fragment_add_post.view.et_created_post_keywords
import kotlinx.android.synthetic.main.fragment_add_post.view.et_created_dorm_description
import com.novatc.ap_app.viewModels.pinboard.AddPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class AddPostFragment: Fragment() {

    private val addPostViewModel: AddPostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
        view.btn_safe_new_post.setOnClickListener {
            if (view.et_post_headline.text.isEmpty()) {
                Toast.makeText(requireContext(), R.string.new_post_missing_headline, Toast.LENGTH_SHORT)
                    .show()
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, "Pls set a post headline", Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
            }
            if (view.et_created_dorm_description.text.isEmpty()) {
                Toast.makeText(requireContext(), R.string.new_post_missing_text, Toast.LENGTH_SHORT).show()
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, "Pls set a post text", Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
            }
            if (view.et_created_post_keywords.text.isEmpty()) {
                Toast.makeText(requireContext(), R.string.new_post_missing_keyword, Toast.LENGTH_SHORT)
                    .show()
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, "Pls set a post keyword", Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
            }
            if (view.et_post_headline.text.isNotEmpty() && view.et_created_dorm_description.text.isNotEmpty() && view.et_created_post_keywords.text.isNotEmpty()) {
                val headline = view.et_post_headline.text.toString()
                val text = view.et_created_dorm_description.text.toString()
                val keyword = view.et_created_post_keywords.text.toString()
                val date = getCurrentDate()
                lifecycleScope.launch {
                    addPostViewModel.addPost(headline, text, keyword, date)
                    val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                    Snackbar.make(bottomNavView, "Post created", Snackbar.LENGTH_SHORT).apply {
                        anchorView = bottomNavView
                    }.show()
                }
                val action = AddPostFragmentDirections.actionAddPostFragmentToFragmentPinboard()
                view.findNavController().navigate(action)

            }

        }
        return view

    }
    fun getCurrentDate():String{
        var current = LocalDateTime.now()
        var fullLocaleFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        var fullLocaleTime = current.format(fullLocaleFormat)
        return fullLocaleTime
    }

    private fun setSavePostButtonListener(view: View) {
        val savePostButton: Button = view.btn_safe_new_post
        savePostButton.setOnClickListener {

        }
    }

}