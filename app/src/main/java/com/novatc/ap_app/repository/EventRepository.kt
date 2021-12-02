package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.EventFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Event
import javax.inject.Inject

class EventRepository
@Inject constructor(
    private val eventFirestore: EventFirestore,
    private val userFirestore: UserFirestore
) {

    fun add(eventName: String, eventDate: String, eventText: String){
        val userId = userFirestore.getCurrentUserID()
        val event = Event(eventName,eventDate, userId, eventText)
        eventFirestore.addEvent(event)
    }
}