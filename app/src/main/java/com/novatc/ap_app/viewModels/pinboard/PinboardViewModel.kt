package com.novatc.ap_app.viewModels.pinboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PinboardViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private var _posts: MutableLiveData<List<Post>> = MutableLiveData()

    init {
        loadPosts()
    }

    // Variable for exposing livedata to other classes
    val postList: LiveData<List<Post>> = _posts


    @ExperimentalCoroutinesApi
    private fun loadPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.getPostsAsFlow().collect { posts ->
                withContext(Dispatchers.Main) {
                    _posts.value = posts
                }

            }
        }
    }


}