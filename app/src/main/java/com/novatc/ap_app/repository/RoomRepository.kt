package com.novatc.ap_app.repository

import android.net.Uri
import android.widget.ImageView
import com.novatc.ap_app.firestore.RoomFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Room
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomFirestore: RoomFirestore,
    private val userFirestore: UserFirestore
) {
    fun add(roomName: String, roomAddress: String, roomDescription: String, minimumBookingTime: String) {
        val userId = userFirestore.getCurrentUserID()
        val room = Room(roomName, roomAddress, userId, roomDescription, minimumBookingTime)
        roomFirestore.addRoom(room)
    }

    fun add(roomName: String, roomAddress: String, roomDescription: String, minimumBookingTime: String, profileImg: String) {
        val userId = userFirestore.getCurrentUserID()
        val room = Room(roomName, roomAddress, userId, roomDescription, minimumBookingTime, profileImg)
        roomFirestore.addRoom(room)
    }

}