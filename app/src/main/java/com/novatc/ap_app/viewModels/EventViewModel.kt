package com.novatc.ap_app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.novatc.ap_app.firestore.EventFirestore
import kotlinx.coroutines.launch
import com.novatc.ap_app.model.EventWithUser
import com.novatc.ap_app.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * The event view model is responsible for loading events from the database
 */
@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = "EventViewModel"
    private var _events: MutableLiveData<ArrayList<EventWithUser>> = MutableLiveData()

    init {
        loadEvents()
    }

    // Variable for exposing livedata to other classes
    internal var events: MutableLiveData<ArrayList<EventWithUser>>
        get() {
            return _events
        }
        set(value) {
            _events = value
        }

    /**
     * Listens for new events in database and populates for each event the event author
     * TODO: Put function into firestore class, database should not be accessed from viewModel
     */
    private fun loadEvents() {
        viewModelScope.launch {
            val eventsWithUser = ArrayList<EventWithUser>()
            eventRepository.getEvents().collect {
                events -> events.forEach{
                eventsWithUser.add(EventWithUser(it.name, it.date, it.text, null))
            }
               _events.value = eventsWithUser
            }
        }

    }


}