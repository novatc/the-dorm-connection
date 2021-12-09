package com.novatc.ap_app.viewModels

import com.novatc.ap_app.firestore.UserFirestore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.firestore.PostFirestore
import com.novatc.ap_app.model.EventWithUser
import kotlinx.coroutines.launch
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import com.squareup.okhttp.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PinboardViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private var _posts:  MutableLiveData<ArrayList<Post>> = MutableLiveData<ArrayList<Post>>()


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