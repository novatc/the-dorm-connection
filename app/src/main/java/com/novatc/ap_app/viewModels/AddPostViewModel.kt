package com.novatc.ap_app.viewModels

import Firestore.Fireclass
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.User

class AddPostViewModel: ViewModel() {

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    init {
        viewModelScope.launch {
            _userProfile.value = Fireclass().getUserData(Fireclass().getCurrentUserID())
        }
    }

}