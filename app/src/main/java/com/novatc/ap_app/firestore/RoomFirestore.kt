package com.novatc.ap_app.firestore

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.*
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.fragments.RoomCreateFragment
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.model.RoomWithUser
import kotlinx.coroutines.tasks.await
import java.net.URI
import java.util.*
import javax.inject.Inject

class RoomFirestore @Inject constructor(){
    private val mFirestore = Firebase.firestore
    var randomKey: String = ""

    suspend fun addRoom(room: Room, context: Context) {
        if (room.imageName != ""){
            randomKey = UUID.randomUUID().toString()
            FirestoreMethods.uploadPicture(Uri.parse(room.imageName), context)
            room.imageName = Uri.parse(room.imageName).pathSegments[Uri.parse(room.imageName).pathSegments.lastIndex].toString()
        }
        val ref = mFirestore.collection(Constants.ROOMS).add(room).await()
        room.key  = ref.id
        Log.e("FIRE", "Created room with id: ${room.key}")
    }

    suspend fun deleteRoom(roomID: String, imageUri: String){
        if(imageUri != ""){
            FirestoreMethods.deletePicture(imageUri)
        }
        mFirestore.collection(Constants.ROOMS).document(roomID).delete().await()
    }
}