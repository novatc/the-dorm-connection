package com.novatc.ap_app.viewModels.you

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _updateNameRequest = MutableLiveData<Request<*>>()
    val updateNameRequest: LiveData<Request<*>> = _updateNameRequest

    private val _updatePasswordRequest = MutableLiveData<Request<*>>()
    val updatePasswordRequest: LiveData<Request<*>> = _updatePasswordRequest

    fun updateName(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.updateUserName(userName)
                withContext(Dispatchers.Main) {
                    _updateNameRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("UpdateName", e.toString())
                withContext(Dispatchers.Main) {
                    _updateNameRequest.value = Request.error(R.string.profile_update_name_error, null)
                }
            }
        }
    }

    fun updatePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.updatePassword(currentPassword, newPassword)
                withContext(Dispatchers.Main) {
                    _updatePasswordRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("UpdatePassword", e.toString())
                withContext(Dispatchers.Main) {
                    if (e.toString().contains("The password is invalid")) {
                        _updatePasswordRequest.value = Request.error(R.string.profile_update_password_incorrect_error, null)
                    } else {
                        _updatePasswordRequest.value = Request.error(R.string.profile_update_password_error, null)
                    }
                }
            }
        }
    }
}