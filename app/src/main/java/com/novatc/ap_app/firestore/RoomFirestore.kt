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
import java.net.URI
import java.util.*
import javax.inject.Inject

class RoomFirestore @Inject constructor(){
    private val mFirestore = Firebase.firestore
    var randomKey: String = ""

    fun addRoom(room: Room, context: Context) {
        if (room.imageName != null){
            randomKey = UUID.randomUUID().toString()
            FirestoreMethods.uploadPicture(Uri.parse(room.imageName), context)
            room.imageName = Uri.parse(room.imageName).pathSegments[Uri.parse(room.imageName).pathSegments.lastIndex].toString()
        }
        mFirestore.collection(Constants.ROOMS).document().set(room, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("ROOM", "Room saved to DB")
            }.addOnFailureListener { e ->
                Log.e("ROOM", "Error while saving: $e")
            }
    }

    /*private fun uploadPicture(profileImg: Uri, context: Context){


        storage = Firebase.storage
        // Create a storage reference from our app
        val storageRef = storage.reference

        val mountainsRef = storageRef.child("mountains.jpg")

        val mountainImagesRef = storageRef.child("images/mountains.jpg")

        mountainsRef.name == mountainImagesRef.name // true
        mountainsRef.path == mountainImagesRef.path // false
        var file = profileImg
        val riversRef = storageRef.child("images/")
        val uploadTask = riversRef.putFile(file)

        uploadTask.addOnFailureListener {
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }*/
}