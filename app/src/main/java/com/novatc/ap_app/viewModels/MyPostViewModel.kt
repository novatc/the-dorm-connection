package com.novatc.ap_app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.Firestore.Fireclass
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.launch

class MyPostViewModel: ViewModel() {
    private val _myPosts = MutableLiveData<ArrayList<Post>>()
    val posts: MutableLiveData<ArrayList<Post>> = _myPosts

    init {
        viewModelScope.launch {
            _myPosts.value = Fireclass().getUserPosts(Fireclass().getCurrentUserID())
        }
    }

}