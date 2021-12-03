package com.novatc.ap_app.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    fun addEvent(eventName: String, eventDate: String, eventText: String) {
        viewModelScope.launch {
            eventRepository.add(eventName, eventDate, eventText)
        }
    }

}