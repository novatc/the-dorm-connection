package com.novatc.ap_app.viewModels.event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.R
import com.novatc.ap_app.model.EventWithUser
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.EventRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _event = MutableLiveData<EventWithUser>()
    val event: LiveData<EventWithUser> = _event

    private val _attendees = MutableLiveData<List<User>>()
    val attendees: LiveData<List<User>> = _attendees

    fun setEvent(event: EventWithUser) {
        this._event.value = event
    }

    private val _switchAttendanceRequest = MutableLiveData<Request<*>>()
    val switchAttendanceRequest: LiveData<Request<*>> = _switchAttendanceRequest

    @ExperimentalCoroutinesApi
    fun loadEventAttendees() {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.getEventAttendees(event.value!!.id!!).collect { users ->
                withContext(Dispatchers.Main) {
                    _attendees.value = users
                }

            }
        }
    }

    fun userAlreadyJoinedEvent(): Boolean {
        val currentUserId = userRepository.readCurrentId()
        if (currentUserId != null) {
            val attendees = attendees.value
            attendees!!.any { it.id == currentUserId }.let {
                return it
            }
        }
        return false
    }

    fun switchEventAttendance() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (userAlreadyJoinedEvent()) {
                    eventRepository.leaveEvent(event.value!!.id!!)
                } else {
                    eventRepository.joinEvent(event.value!!.id!!)
                }
                withContext(Dispatchers.Main) {
                    _switchAttendanceRequest.value = Request.success(null)
                }

            } catch (e: Exception) {
                Log.e("EventDetailsSwitchAttendance", e.toString())
                withContext(Dispatchers.Main) {
                    if (userAlreadyJoinedEvent()) {
                        _switchAttendanceRequest.value =
                            Request.error(R.string.detail_event_leave_error, null)
                    } else {
                        _switchAttendanceRequest.value =
                            Request.error(R.string.detail_event_join_error, null)
                    }
                }
            }
        }
    }


}