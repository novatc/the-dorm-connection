package com.novatc.ap_app.viewModels.pinboard

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
    private val userRepository: UserRepository
) : ViewModel() {
    private var _posts: MutableLiveData<ArrayList<Post>> = MutableLiveData<ArrayList<Post>>()
    private var _userProfile = MutableLiveData<User>()

    init {
        loadPosts()
    }

    // Variable for exposing livedata to other classes
    internal var postsList: MutableLiveData<ArrayList<Post>>
        get() {
            return _posts
        }
        set(value) {
            _posts = value
        }
    internal var userProfile: MutableLiveData<User>
        get() {
            return _userProfile
        }
        set(value) {
            _userProfile = value
        }

    @ExperimentalCoroutinesApi
    private fun loadPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.getPostsAsFlow().collect { posts ->
                val postList = ArrayList<Post>()
                posts.forEach {
                    postList.add(it)
                }
                withContext(Dispatchers.Main) {
                    _posts.value = postList
                }

            }
        }
    }


}