package com.novatc.ap_app.viewModels.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    private var _rooms: MutableLiveData<List<Room>> = MutableLiveData()


    init {
        loadRooms()
    }

        val rooms: LiveData<List<Room>> = _rooms

    @ExperimentalCoroutinesApi
    private fun loadRooms() {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.getRooms().collect { rooms ->
                withContext(Dispatchers.Main) {
                    _rooms.value = rooms
                }

            }
        }

    }




}