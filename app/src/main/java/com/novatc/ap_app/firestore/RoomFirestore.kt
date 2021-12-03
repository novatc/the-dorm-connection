package com.novatc.ap_app.firestore

import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.*
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Room
import java.net.URI
import java.util.*
import javax.inject.Inject

class RoomFirestore @Inject constructor(){
    private val mFirestore = Firebase.firestore
    lateinit var storage: FirebaseStorage
    var randomKey: String = ""

    fun addRoom(room: Room) {
        if (room.mProfileURI != null){
            randomKey = UUID.randomUUID().toString()
            uploadPicture(Uri.parse(room.mProfileURI))
        }
        mFirestore.collection(Constants.ROOMS).document().set(room, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("ROOM", "Room saved to DB")
            }.addOnFailureListener { e ->
                Log.e("ROOM", "Error while saving: $e")
            }
    }

    private fun uploadPicture(profileImg: Uri){

        //val pd = ProgressDialog()

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

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}