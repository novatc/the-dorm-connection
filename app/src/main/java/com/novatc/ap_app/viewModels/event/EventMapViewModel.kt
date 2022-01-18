package com.novatc.ap_app.viewModels.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class EventMapViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private var _events: MutableLiveData<List<Event>> = MutableLiveData()

    init {
        loadEvents()
    }

    // Variable for exposing livedata to the fragment
    val events: LiveData<List<Event>> = _events


    @ExperimentalCoroutinesApi
    private fun loadEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.getEvents().collect { events ->
                withContext(Dispatchers.Main) {
                    _events.value = events
                }

            }
        }

    }
}