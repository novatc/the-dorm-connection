package com.novatc.ap_app.repository

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.UploadTask
import com.novatc.ap_app.constants.UploadDirectories
import com.novatc.ap_app.firestore.RoomFirestore
import com.novatc.ap_app.firestore.StorageFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomFirestore: RoomFirestore,
    private val userFirestore: UserFirestore,
    private val storageFirestore: StorageFirestore
) {
    suspend fun addRoom(roomName: String, streetName: String, houseNumber: String, city:String, roomDescription: String, minimumBookingTime: String, maximumBookingTime: String, imageUri: Uri?): UploadTask.TaskSnapshot? {
        val creatorID = userFirestore.getCurrentUserID() ?: throw Exception("No user id found when trying to add a room.")
        val room = Room(roomName, streetName, houseNumber, city, roomDescription, minimumBookingTime, maximumBookingTime, creatorID)
        val roomId = roomFirestore.addRoom(room)
        return if(imageUri != null) imageUri?.let {
            storageFirestore.uploadImage(UploadDirectories.ROOMS, roomId,
                it
            )
        } else null
    }

    suspend fun deleteRoom(roomID: String)
    {
        roomFirestore.deleteRoom(roomID)
    }

    suspend fun loadRoomImage(roomId: String): String {
        return storageFirestore.loadImage(UploadDirectories.ROOMS, roomId)
    }

    @ExperimentalCoroutinesApi
    fun getRooms(): Flow<List<Room>> {
        return roomFirestore.getRoomsFlow()
    }

    suspend fun addBooking(roomID: String, booking: Booking ): DocumentReference? {
        return roomFirestore.addBooking(roomID, booking = booking)
    }

    @ExperimentalCoroutinesApi
    fun getBookingsAsFlow(roomID: String): Flow<List<Booking>> {
        return roomFirestore.getBookingsFlow(roomID)
    }

}