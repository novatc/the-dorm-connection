package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.RoomFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Room
import java.lang.Exception
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomFirestore: RoomFirestore,
    private val userFirestore: UserFirestore
) {
    fun add(roomName: String, roomAddress: String, roomDescription: String, minimumBookingTime: String) {
        val userId = userFirestore.getCurrentUserID() ?: throw Exception("No user id, when trying to add a room.")
        val room = Room(roomName, roomAddress, userId, roomDescription, minimumBookingTime)
        roomFirestore.addRoom(room)
    }

    fun add(roomName: String, roomAddress: String, roomDescription: String, minimumBookingTime: String, profileImg: String) {
        val userId = userFirestore.getCurrentUserID() ?: throw Exception("No user id, when trying to add a room.")
        val room = Room(roomName, roomAddress, userId, roomDescription, minimumBookingTime, profileImg)
        roomFirestore.addRoom(room)
    }

}