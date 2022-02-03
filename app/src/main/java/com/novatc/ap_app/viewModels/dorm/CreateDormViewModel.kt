package com.novatc.ap_app.viewModels.dorm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateDormViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    init {
        viewModelScope.launch {
            _userProfile.value = userRepository.readCurrent()
        }
    }


}