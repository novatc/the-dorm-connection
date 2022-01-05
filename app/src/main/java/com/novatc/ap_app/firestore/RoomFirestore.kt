package com.novatc.ap_app.firestore

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.Room
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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

    fun loadPicture(imageView: ImageView, imageName: String?, context: Context?){
        if (imageName != null) {
            FirestoreMethods.loadPicture(imageView, imageName, context)
        }
    }

    @ExperimentalCoroutinesApi
    fun getRoomsFlow(): Flow<List<Room>> {
        return mFirestore
            .collection(Constants.ROOMS)
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getRoomFromSnapshot(it)
                } ?: listOf()
            }
    }

    @ExperimentalCoroutinesApi
    fun CollectionReference.getQuerySnapshotFlow(): Flow<QuerySnapshot?> {
        return callbackFlow {
            val listenerRegistration =
                addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("Snapshot Flow", "Error fetchting collect = $path")
                        return@addSnapshotListener
                    }
                    trySend(querySnapshot).isSuccess
                }
            awaitClose {
                Log.e("", "cancelling the listener on collection at path - $path")
                listenerRegistration.remove()
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun <T> CollectionReference.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
        return getQuerySnapshotFlow()
            .map { snapshot ->
                return@map mapper(snapshot)
            }
    }

    // Parses the document snapshot to the desired object
    fun getRoomFromSnapshot(documentSnapshot: DocumentSnapshot): Room {
        return documentSnapshot.toObject(Room::class.java)!!.let {
            it.id = documentSnapshot.id
            return@let it
        }
    }
}