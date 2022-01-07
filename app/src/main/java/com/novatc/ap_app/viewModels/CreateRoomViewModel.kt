package com.novatc.ap_app.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    fun addRoom(roomName: String,
                roomAddress: String,
                roomDescription: String,
                minimumBookingTime: String,
                imageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            roomRepository.addRoom(roomName, roomAddress, roomDescription, minimumBookingTime, imageUri)
        }
    }

}