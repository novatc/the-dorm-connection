package com.novatc.ap_app.firestore

import android.util.Log
import com.google.firebase.firestore.*
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EventFirestore @Inject constructor(
    private val helperFirestore: HelperFirestore
) {
    private val mFirestore = FirebaseFirestore.getInstance()

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

    @ExperimentalCoroutinesApi
    fun getEventsFlow(dormId: String): Flow<List<Event>> {
        return mFirestore
            .collection(Constants.EVENTS).whereEqualTo("dormId", dormId)
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
            return@let it
        }
    }

    suspend fun addEvent(event: Event): String {
        return mFirestore.collection(Constants.EVENTS).add(event).await().id
    }

    fun getEventAttendeesFlow(eventId: String): Flow<List<User>> {
        return mFirestore.collection(Constants.EVENTS).document(eventId)
            .collection(Constants.ATTENDEES).getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getAttendeesFromSnapshot(it)
                } ?: listOf()
            }
    }

    fun getAttendeesFromSnapshot(documentSnapshot: DocumentSnapshot): User {
        return documentSnapshot.toObject(User::class.java)!!.let {
            it.id = documentSnapshot.id
            return@let it
        }
    }

    suspend fun removeUserFromEvent(userId: String, eventId: String): Void? {
        return mFirestore.collection(Constants.EVENTS).document(eventId)
            .collection(Constants.ATTENDEES).document(userId).delete().await()
    }

    suspend fun addUserToEvent(user: User, eventId: String): Void? {
        return mFirestore.collection(Constants.EVENTS).document(eventId)
            .collection(Constants.ATTENDEES).document(user.id).set(user).await()
    }

    suspend fun deleteEvent(eventId: String): Void? {
        val attendees = mFirestore.collection(Constants.EVENTS).document(eventId).collection(Constants.ATTENDEES)
        helperFirestore.deleteCollection(attendees, 5)
        return mFirestore.collection(Constants.EVENTS).document(eventId).delete().await()
    }


}