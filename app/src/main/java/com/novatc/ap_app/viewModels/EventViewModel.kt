package com.novatc.ap_app.viewModels

import com.novatc.ap_app.Constants.Constants
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.novatc.ap_app.fragments.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.EventWithUser
import com.novatc.ap_app.model.User

/**
 * The event view model is responsible for loading events from the database
 */
class EventViewModel : ViewModel() {

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
        fireStore.collection(Constants.EVENTS).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen for events failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val documents = snapshot.documents
                // Load event authors async in coroutine
                viewModelScope.launch(Dispatchers.IO) {
                    val allEvents = ArrayList<EventWithUser>()
                    documents.forEach {
                        val event = it.toObject(Event::class.java)
                        if (event != null) {
                            val user = fireStore.collection(Constants.USER)
                                .document(event.userId)
                                .get()
                                .await()
                                .toObject(User::class.java)
                            val eventWithUser =
                                EventWithUser(event.name, event.date, event.text, user)
                                allEvents.add(eventWithUser)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        _events.value = allEvents
                    }
                }


            }
        }

    }


}