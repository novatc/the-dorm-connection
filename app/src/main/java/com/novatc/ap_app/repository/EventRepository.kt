package com.novatc.ap_app.repository

import android.net.Uri
import com.google.firebase.storage.UploadTask
import com.novatc.ap_app.constants.UploadDirectories
import com.novatc.ap_app.firestore.EventFirestore
import com.novatc.ap_app.firestore.StorageFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class EventRepository
@Inject constructor(
    private val eventFirestore: EventFirestore,
    private val userFirestore: UserFirestore,
    private val storageFirestore: StorageFirestore
) {

    // Adds a new event and uploads an image
    suspend fun add(
        eventName: String,
        eventDate: String,
        eventText: String,
        eventStreet: String,
        eventHouseNumber: String,
        eventCity: String,
        imageUri: Uri?,
    ): UploadTask.TaskSnapshot? {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to create an event.")
        val user = userFirestore.getUserData(userId)
        val event = Event(
            name = eventName,
            date =
            eventDate,
            authorId = userId,
            authorName = user!!.username,
            text = eventText,
            streetName = eventStreet,
            houseNumber = eventHouseNumber,
            city = eventCity
        )
        val eventId = eventFirestore.addEvent(event)
        return if(imageUri != null) storageFirestore.uploadImage(UploadDirectories.EVENTS, eventId, imageUri) else null

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

    suspend fun deleteEventImage(eventId: String): Void? {
        return storageFirestore.deleteImage(UploadDirectories.EVENTS, eventId)
    }

    suspend fun loadEventImage(eventId: String): String {
        return storageFirestore.loadImage(UploadDirectories.EVENTS, eventId)
    }
}