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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class WgViewModel @Inject constructor(
    private val wgRepository: WgRepository
) : ViewModel() {

    private var _joinWgRequest = MutableLiveData<Request<*>>()
    val joinWgRequest: LiveData<Request<*>> = _joinWgRequest

    private var _wgs: MutableLiveData<List<Wg>> = MutableLiveData()
    val wgs: LiveData<List<Wg>> = _wgs

    init {
        loadWgs()
    }

    fun joinWg(wgId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                wgRepository.joinWg(wgId)
                withContext(Dispatchers.Main) {
                    _joinWgRequest.value = Request.success(null)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    if (e.message == "Could not find any wg with the supplied id") {
                        _joinWgRequest.value = Request.error(R.string.wg_list_join_wg_error, null)
                    } else if (e.message == "Wg is not part of users dorm.") {
                        _joinWgRequest.value = Request.error(R.string.wg_list_join_id_dorm_mismatch_error, null)
                    } else {
                        _joinWgRequest.value = Request.error(R.string.wg_list_join_error, null)
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun loadWgs() {
        viewModelScope.launch(Dispatchers.IO) {
            wgRepository.getWgs().collect { events ->
                withContext(Dispatchers.Main) {
                    _wgs.value = events
                }
            }
        }
    }


}