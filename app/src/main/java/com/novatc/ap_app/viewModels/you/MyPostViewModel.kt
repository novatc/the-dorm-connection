package com.novatc.ap_app.viewModels.you

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(
    postRepository: PostRepository
) : ViewModel() {
    private val _myPosts = MutableLiveData<ArrayList<Post>>()
    val posts: MutableLiveData<ArrayList<Post>> = _myPosts

    init {
        viewModelScope.launch {
            _myPosts.value = postRepository.getUserPosts()
        }
    }

}