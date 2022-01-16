package com.novatc.ap_app.viewModels.wg

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.Wg
import com.novatc.ap_app.repository.WgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WgDetailViewModel @Inject constructor(
    private val wgRepository: WgRepository
): ViewModel() {

    private var _getWgRequest = MutableLiveData<Request<Wg?>>()
    val getWgRequest: LiveData<Request<Wg?>> = _getWgRequest

    private var _leaveWgRequest =  MutableLiveData<Request<*>>()
    val leaveWgRequest: LiveData<Request<*>> = _leaveWgRequest

    fun getWg(wgId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val wg = wgRepository.getWg(wgId)
                withContext(Dispatchers.Main) {
                    _getWgRequest.value = Request.success(wg)
                }
            } catch (e: Exception) {
                Log.e("WgDetail", e.toString())
                withContext(Dispatchers.Main) {
                    _getWgRequest.value = Request.error(R.string.wg_detail_load_error, null)
                }
            }
        }
    }

    fun leaveWg() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                wgRepository.leaveWg()
                withContext(Dispatchers.Main) {
                    _leaveWgRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("LeaveWg", e.toString())
                withContext(Dispatchers.Main) {
                    _leaveWgRequest.value = Request.error(R.string.wg_list_leave_error, null)
                }
            }
        }
    }
}