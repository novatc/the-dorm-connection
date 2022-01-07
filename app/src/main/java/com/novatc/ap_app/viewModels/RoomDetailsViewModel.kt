package com.novatc.ap_app.viewModels

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _room = MutableLiveData<Room>()
    val room: LiveData<Room> = _room

    fun setRoom(selectedRoom: Room) {
        this._room.value = selectedRoom
    }

    private val _loadImageRequest = MutableLiveData<Request<String?>>()
    val loadImageRequest: LiveData<Request<String?>> = _loadImageRequest

    suspend fun deleteRoom(roomID: String){
        roomRepository.deleteRoom(roomID)
    }

    fun loadRoomImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _room.value?.id?.let {
                    val imageUri = roomRepository.loadRoomImage(it)
                    Log.e("LoadRoomImage", "test")
                    withContext(Dispatchers.Main) {
                        _loadImageRequest.value = Request.success(imageUri)
                    }
                }
            } catch (e: Exception) {
                Log.e("LoadRoomImage", "Could not load event image with id ${_room.value?.id}")
                withContext(Dispatchers.Main) {
                    _loadImageRequest.value = Request.error(null, null)
                }

            }
        }
    }

}