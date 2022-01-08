package com.novatc.ap_app.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private var _comments: MutableLiveData<ArrayList<Comment>> = MutableLiveData<ArrayList<Comment>>()

    init {
        loadComments()
    }

    // Variable for exposing livedata to other classes
    internal var commentList: MutableLiveData<ArrayList<Comment>>
        get() {
            return _comments
        }
        set(value) {
            _comments = value
        }

    suspend fun deletePost(postID: String){
        postRepository.deletePost(postID)
    }

    fun readCurrentID(): String? {
        return userRepository.readCurrentId()
    }

    private fun loadComments() {
        Log.e("TEST", "TEST")
    }

    suspend fun addComment(postID: String, comment: Comment): DocumentReference? {
        return postRepository.addComment(postID, comment)
    }
}