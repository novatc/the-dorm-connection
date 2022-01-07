package com.novatc.ap_app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository
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

    private fun loadComments() {
        TODO("Not yet implemented")
    }


}