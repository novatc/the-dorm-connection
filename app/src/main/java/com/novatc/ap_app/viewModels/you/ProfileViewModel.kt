package com.novatc.ap_app.viewModels.you

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import kotlinx.coroutines.launch
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile
    val deleteRequest = MutableLiveData<Request<*>>()

    init {
        viewModelScope.launch {
            _userProfile.value = userRepository.readCurrent()
        }
    }

    fun deleteUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.delete()
                withContext(Dispatchers.Main) {
                    deleteRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("Delete user", e.toString())
                withContext(Dispatchers.Main) {
                    deleteRequest.value = Request.error(R.string.delete_user_error, null)
                }
            }

        }

    }


}