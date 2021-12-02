package com.novatc.ap_app.firestore

import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Room
import javax.inject.Inject

class RoomFirestore @Inject constructor(){
    private val mFirestore = Firebase.firestore

    fun addRoom(room: Room) {
        mFirestore.collection(Constants.ROOMS).document().set(room, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("ROOM", "Room saved to DB")
            }.addOnFailureListener { e ->
                Log.e("ROOM", "Error while saving: $e")
            }
    }
}