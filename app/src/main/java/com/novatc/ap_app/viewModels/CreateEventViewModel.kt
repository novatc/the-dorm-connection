package com.novatc.ap_app.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.EventRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun addEvent(
        eventName: String,
        eventDate: String,
        eventText: String,
        eventStreet: String,
        eventHouseNumber: String,
        eventCity: String,

    ) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.readCurrent()?.let {
                eventRepository.add(eventName, eventDate, eventText, eventStreet, eventHouseNumber, eventCity,
                    it
                )
            }
        }
    }

}