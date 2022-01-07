package com.novatc.ap_app.viewModels

import androidx.lifecycle.ViewModel
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun deletePost(postID: String){
        postRepository.deletePost(postID)
    }

    fun readCurrentID(): String? {
        return userRepository.readCurrentId()
    }
}