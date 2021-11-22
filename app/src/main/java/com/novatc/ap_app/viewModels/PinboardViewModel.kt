package com.novatc.ap_app.viewModels

import Firestore.Fireclass
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.Post
import model.User

class PinboardViewModel: ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    init {
        viewModelScope.launch {
            _posts.value = Fireclass().getPosts()
        }
    }
}