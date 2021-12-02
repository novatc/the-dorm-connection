package com.novatc.ap_app.viewModels

import com.novatc.ap_app.firestore.UserFirestore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.firestore.PostFirestore
import kotlinx.coroutines.launch
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.PostRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinboardViewModel @Inject constructor(
    postRepository: PostRepository
): ViewModel() {
    private val _posts = MutableLiveData<ArrayList<Post>>()
    val posts: MutableLiveData<ArrayList<Post>> = _posts

    init {
        viewModelScope.launch {
            _posts.value = postRepository.getPosts()
        }
    }
}