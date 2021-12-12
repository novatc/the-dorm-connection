package com.novatc.ap_app.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventFirestore @Inject constructor(
    private val userFirestore: UserFirestore
) {
    private val mFirestore = FirebaseFirestore.getInstance()

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

    @ExperimentalCoroutinesApi
    fun getEventsFlow(): Flow<List<Event>> {
        return mFirestore
            .collection("events")
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getEventFromSnapshot(it)
                } ?: listOf()
            }
    }

    // Parses the document snapshot to the desired object
    fun getEventFromSnapshot(documentSnapshot: DocumentSnapshot): Event {
        return documentSnapshot.toObject(Event::class.java)!!.let {
            it.id = documentSnapshot.id
            Log.e("EVENT", "transformed Event from Snapshot: ${it}")
            return@let it
        }

    }

    fun addEvent(event: Event): Task<Void> {
        return mFirestore.collection(Constants.EVENTS).document().set(event, SetOptions.merge())
    }

    fun updateUserList(userList: ArrayList<User>, eventID: String) {
        val dbEvent = mFirestore.collection(Constants.EVENTS).document(eventID)
        dbEvent.update("userList", userList)

    }


}