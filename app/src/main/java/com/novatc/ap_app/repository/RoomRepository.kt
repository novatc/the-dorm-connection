package com.novatc.ap_app.repository

import android.content.Context
import android.widget.ImageView
import com.novatc.ap_app.firestore.RoomFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.Room
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomFirestore: RoomFirestore,
    private val userFirestore: UserFirestore
) {
    suspend fun addRoom(roomName: String, roomAddress: String, roomDescription: String, minimumBookingTime: String, profileImg: String, context: Context) {
        val creatorID = userFirestore.getCurrentUserID() ?: throw Exception("No user id found when trying to add a room.")
        val room = Room(roomName, roomAddress, roomDescription, minimumBookingTime, profileImg, creatorID)
        roomFirestore.addRoom(room, context)
    }

    suspend fun deleteRoom(roomID: String, imageUri: String)
    {
        roomFirestore.deleteRoom(roomID, imageUri)
    }

    fun loadPicture(imageView: ImageView, imageName: String?, context: Context?){
        roomFirestore.loadPicture(imageView, imageName, context)
    }

    @ExperimentalCoroutinesApi
    fun getRooms(): Flow<List<Room>> {
        return roomFirestore.getRoomsFlow()
    }

}