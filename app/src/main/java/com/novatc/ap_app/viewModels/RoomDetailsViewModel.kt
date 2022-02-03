package com.novatc.ap_app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.room.RoomDetailsBookFragment
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

    private val _bookings = MutableLiveData<List<Booking>>()
    val bookings: LiveData<List<Booking>> = _bookings

    private val _deleteRoomRequest = MutableLiveData<Request<*>>()
    val deleteRoomRequest: LiveData<Request<*>> = _deleteRoomRequest

    fun setRoom(selectedRoom: Room) {
        this._room.value = selectedRoom
        viewModelScope.launch {
            _userProfile.value = userRepository.readCurrent()
        }
    }

    private val _loadImageRequest = MutableLiveData<Request<String?>>()
    val loadImageRequest: LiveData<Request<String?>> = _loadImageRequest

    fun deleteRoom(){
        val currentRoomId = _room.value?.id!!
        viewModelScope.launch(Dispatchers.IO) {
            try {
                roomRepository.deleteRoom(currentRoomId)
                roomRepository.deleteRoomImage(currentRoomId)
                withContext(Dispatchers.Main) {
                    _deleteRoomRequest.value = Request.success(null)
                }
            } catch (e: Exception) {
                Log.e("RoomDetailsViewModel", e.toString())
                withContext(Dispatchers.Main) {
                    _deleteRoomRequest.value =
                        Request.error(R.string.details_event_delete_error, null)
                }
            }

        }
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

    fun loadBookings(roomDetailsBookFragment: RoomDetailsBookFragment) {
        Log.e("ROOM", "Room is ${room.value}")
        viewModelScope.launch(Dispatchers.IO) {
            val bookingList = ArrayList<Booking>()
            roomRepository.getBookingsAsFlow(room.value!!.id!!).collect { bookings ->
                if (bookings.isEmpty()) {
                    Log.e("BOOKINGS", "NO BOOKINGS")
                } else {
                    bookings.forEach {
                        bookingList.add(it)
                    }
                }
                    withContext(Dispatchers.Main) {
                        _bookings.value = bookingList
                        roomDetailsBookFragment.populateCalendar()
                    }
            }
        }
    }

    suspend fun addBooking(roomID: String, booking: Booking): DocumentReference? {
        return roomRepository.addBooking(roomID, booking)
    }

}