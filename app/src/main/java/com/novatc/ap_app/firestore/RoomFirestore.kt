package com.novatc.ap_app.firestore

import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Booking
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.model.Room
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class RoomFirestore @Inject constructor(
    private val helperFirestore: HelperFirestore
){
    private val mFirestore = Firebase.firestore

    suspend fun addRoom(room: Room): String {
        return mFirestore.collection(Constants.ROOMS).add(room).await().id
    }

    suspend fun deleteRoom(roomID: String): Void? {
        val bookings = mFirestore.collection(Constants.ROOMS).document(roomID).collection(Constants.BOOKINGS)
        helperFirestore.deleteCollection(bookings, 5)
        return mFirestore.collection(Constants.ROOMS).document(roomID).delete().await()
    }

    @ExperimentalCoroutinesApi
    fun getRoomsFlow(dormId: String): Flow<List<Room>> {
        return mFirestore
            .collection(Constants.ROOMS).whereEqualTo("dormId", dormId)
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getRoomFromSnapshot(it)
                } ?: listOf()
            }
    }

    @ExperimentalCoroutinesApi
    fun Query.getQuerySnapshotFlow(): Flow<QuerySnapshot?> {
        return callbackFlow {
            val listenerRegistration =
                addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("Snapshot Flow", "Error fetchting collect")
                        return@addSnapshotListener
                    }
                    trySend(querySnapshot).isSuccess
                }
            awaitClose {
                Log.e("", "cancelling the listener on collection")
                listenerRegistration.remove()
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun <T> Query.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
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

    suspend fun addBooking(roomID: String, booking: Booking): DocumentReference? {
        return mFirestore.collection(Constants.ROOMS).document(roomID).collection(Constants.BOOKINGS)
            .add(booking).await()
    }

    fun getBookingsFlow(roomID: String): Flow<List<Booking>> {
        return mFirestore.collection(Constants.ROOMS).document(roomID).collection(Constants.BOOKINGS)
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getBookingsFromSnapshot(it)
                } ?: listOf()
            }

    }

    private fun getBookingsFromSnapshot(documentSnapshot: DocumentSnapshot): Booking {
        return documentSnapshot.toObject(Booking::class.java)!!.let {
            it.id = documentSnapshot.id
            return@let it
        }
    }

    suspend fun getUserBookings(userID: String): ArrayList<Booking> {
        val bookings = ArrayList<Booking>()
        val snapshot1 =  mFirestore.collection(Constants.ROOMS).get().await()
        for (room in snapshot1){
            var bookingsInRoom = getBookingsFlow(room.id)
            bookingsInRoom.collect {
                booking ->
                booking.forEach {
                    b ->
                    if(b.userID == userID){
                        bookings.add(b)
                    }
                }
            }
        }
        return bookings
    }
}