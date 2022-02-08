package com.novatc.ap_app.viewModels.pinboard

import androidx.lifecycle.ViewModel
import com.novatc.ap_app.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    suspend fun addPost(headline: String, text: String, keyword: String, date: Long){
        postRepository.addPost(headline, text,keyword,date)
    }
}