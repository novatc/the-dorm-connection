package com.novatc.ap_app.repository

import android.content.ClipDescription
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
    suspend fun addRoom(roomName: String, streetName: String, houseNumber: String, city:String, roomShortDescription: String, roomDescription: String, minimumBookingTime: String, maximumBookingTime: String, imageUri: Uri?): UploadTask.TaskSnapshot? {
        val userId = userFirestore.getCurrentUserID() ?: throw Exception("No user id found when trying to add a room.")
        val user = userFirestore.getUserData(userId)
        val room = Room(roomName, streetName, houseNumber, city, roomShortDescription, roomDescription, minimumBookingTime, maximumBookingTime, userId, user!!.userDormID)
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

    suspend fun deleteRoomImage(roomID: String): Void? {
        return storageFirestore.deleteImage(UploadDirectories.ROOMS, roomID)
    }

    suspend fun loadRoomImage(roomId: String): String {
        return storageFirestore.loadImage(UploadDirectories.ROOMS, roomId)
    }

    @ExperimentalCoroutinesApi
    suspend fun getRooms(): Flow<List<Room>> {
        val userId = userFirestore.getCurrentUserID() ?: throw Exception("No user id found when fetch rooms.")
        val user = userFirestore.getUserData(userId)
        return roomFirestore.getRoomsFlow(user!!.userDormID)
    }

    suspend fun addBooking(roomID: String, booking: Booking ): DocumentReference? {
        return roomFirestore.addBooking(roomID, booking = booking)
    }

    suspend fun getUserBookings(): ArrayList<Booking> {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to get user posts.")
        return roomFirestore.getUserBookings(userId)
    }

    @ExperimentalCoroutinesApi
    fun getBookingsAsFlow(roomID: String): Flow<List<Booking>> {
        return roomFirestore.getBookingsFlow(roomID)
    }

}