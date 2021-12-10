package com.novatc.ap_app.viewModels;

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.repository.UserRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var signupRequest: MutableLiveData<Request<*>> = MutableLiveData()

    fun signUpUser(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.signUp(name, email, password)
                withContext(Dispatchers.Main) {
                    signupRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("Signup", e.toString())
                withContext(Dispatchers.Main) {
                    signupRequest.value = Request.error(R.string.sign_up_error, null)
                }
            }

        }
    }
}