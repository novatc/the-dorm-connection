package com.novatc.ap_app.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.novatc.ap_app.firestore.EventFirestore
import kotlinx.coroutines.launch
import com.novatc.ap_app.model.EventWithUser
import com.novatc.ap_app.repository.EventRepository
import com.novatc.ap_app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * The event view model is responsible for loading events from the database
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository
) : ViewModel() {

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
     */
    @ExperimentalCoroutinesApi
    private fun loadEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.getEvents().collect { events ->
                val eventsWithUser = ArrayList<EventWithUser>()
                events.forEach {
                    Log.e("EVENT", "Event: ${it}")
                    val user = it.userId?.let { it1 -> userRepository.read(it1) }
                    Log.e("EVENT", "User: ${user}")
                    if (user != null) {
                        eventsWithUser.add(
                            EventWithUser(
                                name = it.name,
                                date = it.date,
                                text = it.text,
                                user = user
                            )
                        )
                    }
                }
                withContext(Dispatchers.Main) {
                    _events.value = eventsWithUser
                }

            }
        }

    }


}