package com.novatc.ap_app.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.novatc.ap_app.fragments.*
import model.Event

class EventViewModel : ViewModel() {

    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = "EventViewModel"
    private var _events: MutableLiveData<ArrayList<Event>> = MutableLiveData()

    init {
        loadEvents()
    }

    internal var events: MutableLiveData<ArrayList<Event>>
        get() {
            return _events
        }
        set(value) {
            _events = value
        }

    private fun loadEvents() {
        fireStore.collection("events").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen for events failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val allEvents = ArrayList<Event>()
                val documents = snapshot.documents
                documents.forEach {
                    val event = it.toObject(Event::class.java)
                    if (event != null) {
                        allEvents.add(event)
                    }
                }
                _events.value = allEvents
            }
        }

    }
}