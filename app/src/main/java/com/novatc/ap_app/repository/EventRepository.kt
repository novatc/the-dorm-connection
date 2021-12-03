package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.EventFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventRepository
@Inject constructor(
    private val eventFirestore: EventFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun add(eventName: String, eventDate: String, eventText: String) {
        val userId = userFirestore.getCurrentUserID()
        val event = Event(eventName,eventDate, userId, eventText)
        return withContext(Dispatchers.IO) {
            eventFirestore.addEvent(event).await()
        }
    }

    @ExperimentalCoroutinesApi
    fun getEvents(): Flow<List<Event>> {
        return eventFirestore.getEventsFlow()
    }
}