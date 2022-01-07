package com.novatc.ap_app.viewModels.wg

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.repository.WgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateWgViewModel @Inject constructor(
    private val wgRepository: WgRepository
): ViewModel() {

    private var _createWgRequest =  MutableLiveData<Request<*>>()
    val createEventRequest: LiveData<Request<*>> = _createWgRequest

    fun createWg(name: String, slogan: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                wgRepository.createWg(name, slogan)
                withContext(Dispatchers.Main) {
                    _createWgRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("CreateWg", e.toString())
                withContext(Dispatchers.Main) {
                    _createWgRequest.value = Request.error(R.string.wg_create_failure, null)
                }
            }
        }
    }
}