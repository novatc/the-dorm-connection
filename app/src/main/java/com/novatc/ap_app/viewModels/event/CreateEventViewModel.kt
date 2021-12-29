package com.novatc.ap_app.viewModels.event

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.repository.EventRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    fun addEvent(
        eventName: String,
        eventDate: String,
        eventText: String,
        eventStreet: String,
        eventHouseNumber: String,
        eventCity: String,
        imageUri: Uri?

    ) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.add(
                eventName, eventDate, eventText, eventStreet, eventHouseNumber, eventCity, imageUri
            )
        }
    }


}