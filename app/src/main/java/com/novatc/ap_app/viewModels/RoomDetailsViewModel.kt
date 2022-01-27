package com.novatc.ap_app.viewModels

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.novatc.ap_app.model.*
import com.novatc.ap_app.repository.RoomRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _room = MutableLiveData<Room>()
    val room: LiveData<Room> = _room

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    private var _bookings: MutableLiveData<ArrayList<Booking>> =
        MutableLiveData<ArrayList<Booking>>()

    fun setRoom(selectedRoom: Room) {
        this._room.value = selectedRoom
        viewModelScope.launch {
            _userProfile.value = userRepository.readCurrent()
        }
    }

    private val _loadImageRequest = MutableLiveData<Request<String?>>()
    val loadImageRequest: LiveData<Request<String?>> = _loadImageRequest

    suspend fun deleteRoom(roomID: String){
        roomRepository.deleteRoom(roomID)
    }

    internal var bookingList: MutableLiveData<ArrayList<Booking>>
        get() {
            return _bookings
        }
        set(value) {
            _bookings = value
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

    @ExperimentalCoroutinesApi
    fun loadBookings() {
        _bookings
        Log.e("ROOM", "Room is ${room.value}")
        viewModelScope.launch(Dispatchers.IO) {
            val bookingList = ArrayList<Booking>()
            roomRepository.getBookingsAsFlow(room.value?.id!!).collect { bookings ->
                if (bookings.isEmpty()) {
                    Log.e("BOOKINGS", "NO BOOKINGS")
                } else {
                    bookings.forEach {
                        bookingList.add(it)
                    }
                }
                    withContext(Dispatchers.Main) {
                        _bookings.value = bookingList
                    }
            }
        }
    }

    suspend fun addBooking(roomID: String, booking: Booking): DocumentReference? {
        return roomRepository.addBooking(roomID, booking)
    }

}