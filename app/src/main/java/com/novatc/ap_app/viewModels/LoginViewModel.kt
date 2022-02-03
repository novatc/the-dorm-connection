package com.novatc.ap_app.viewModels;

import com.novatc.ap_app.model.Request
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.R
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.UserRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var loginRequest: MutableLiveData<Request<*>> = MutableLiveData()
    private val _me = MutableLiveData<User>()
    var me: LiveData<User> = _me
    fun getCurrentFirebaseUser(): String? {
        return userRepository.readCurrentId()
    }


    fun loadUserObject() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.readCurrent()
            if (user != null) {
                withContext(Dispatchers.Main) {
                    _me.value = user!!
                }
            }

        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.login(email, password)
                withContext(Dispatchers.Main) {
                    loginRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("Login", e.toString())
                withContext(Dispatchers.Main) {
                    loginRequest.value = Request.error(R.string.login_error, null)
                }
            }
        }
    }
}