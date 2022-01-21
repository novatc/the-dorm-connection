package com.novatc.ap_app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private var _comments: MutableLiveData<ArrayList<Comment>> =
        MutableLiveData<ArrayList<Comment>>()
    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile


    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    fun setPost(post_: Post) {
        this._post.value = post_
        viewModelScope.launch {
            _userProfile.value = userRepository.readCurrent()
        }
    }

    // Variable for exposing livedata to other classes
    internal var commentList: MutableLiveData<ArrayList<Comment>>
        get() {
            return _comments
        }
        set(value) {
            _comments = value
        }

    suspend fun deletePost(postID: String) {
        postRepository.deletePost(postID)
    }

    fun readCurrentID(): String? {
        return userRepository.readCurrentId()
    }


    @ExperimentalCoroutinesApi
    fun loadComments() {
        Log.e("POST", "Post is ${post.value}")
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.getCommentsAsFlow(post.value?.key!!).collect { comments ->
                if (comments.isEmpty()) {
                    Log.e("COMMENTS", "NO Comments")
                } else {
                    val commentList = ArrayList<Comment>()
                    comments.forEach {
                        commentList.add(it)
                    }
                    withContext(Dispatchers.Main) {
                        _comments.value = commentList
                    }
                }

            }
        }
    }

    suspend fun addComment(postID: String, comment: Comment): DocumentReference? {
        return postRepository.addComment(postID, comment)
    }
}