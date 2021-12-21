package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.EventFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class EventRepository
@Inject constructor(
    private val eventFirestore: EventFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun add(
        eventName: String,
        eventDate: String,
        eventText: String,
        eventStreet: String,
        eventHouseNumber: String,
        eventCity: String,
        user: User
    ) {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to create an event.")
        val event = Event(
            name = eventName,
            date =
            eventDate,
            userId = userId,
            text = eventText,
            streetName = eventStreet,
            houseNumber = eventHouseNumber,
            city = eventCity
        )
        event.addUser(user)
        return withContext(Dispatchers.IO) {
            eventFirestore.addEvent(event).await()
        }
    }

    @ExperimentalCoroutinesApi
    fun getEvents(): Flow<List<Event>> {
        return eventFirestore.getEventsFlow()
    }

    @ExperimentalCoroutinesApi
    fun getEventAttendees(eventId: String): Flow<List<User>> {
        return eventFirestore.getEventAttendeesFlow(eventId)
    }

    suspend fun leaveEvent(eventId: String): Void? {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to remove user from event")
        return eventFirestore.removeUserFromEvent(userId, eventId)
    }

    suspend fun joinEvent(eventId: String): Void? {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to add user to event")
        val user = userFirestore.getUserData(userId)
            ?: throw Exception("No user, when trying to add user to event")
        return eventFirestore.addUserToEvent(user, eventId)
    }

    suspend fun deleteEvent(eventId: String): Void? {
        return eventFirestore.deleteEvent(eventId)
    }
}