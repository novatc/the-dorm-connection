package com.novatc.ap_app.viewModels

import Constants.Constants
import Firestore.Fireclass
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.novatc.ap_app.fragments.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import model.Event
import model.EventWithUser
import model.User

class EventViewModel : ViewModel() {

    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = "EventViewModel"
    private var _events: MutableLiveData<ArrayList<EventWithUser>> = MutableLiveData()

    init {
        loadEvents()
    }

    internal var events: MutableLiveData<ArrayList<EventWithUser>>
        get() {
            return _events
        }
        set(value) {
            _events = value
        }

    private fun loadEvents() {
        fireStore.collection(Constants.EVENTS).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen for events failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val documents = snapshot.documents
                GlobalScope.launch(Dispatchers.IO) {
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