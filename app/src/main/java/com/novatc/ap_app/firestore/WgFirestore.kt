package com.novatc.ap_app.firestore

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.User
import com.novatc.ap_app.model.Wg
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.lang.NullPointerException
import javax.inject.Inject

class WgFirestore @Inject constructor() {

    private val mFirestore = Firebase.firestore

    suspend fun createWg(wg: Wg) {
        mFirestore.collection(Constants.WG).add(wg).await()
    }

    suspend fun getWg(wgId: String): Wg {
        try {
            val documentSnapshot =  mFirestore.collection(Constants.WG).document(wgId).get().await()
            return documentSnapshot.toObject(Wg::class.java)!!.let {
                it.id = documentSnapshot.id
                return@let it
            }
        } catch (e: NullPointerException) {
            throw NullPointerException("Could not find any wg with the supplied id")
        }
    }

    suspend fun joinWg(wgId: String, user: User) {
        mFirestore.collection(Constants.WG).document(wgId)
            .collection(Constants.MEMBERS)
            .document(user.id).set(user).await()
    }

    suspend fun leaveWg(wgId: String, userId: String) {
        mFirestore.collection(Constants.WG).document(wgId)
            .collection(Constants.MEMBERS)
            .document(userId).delete().await()
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

    @ExperimentalCoroutinesApi
    fun getWgsFlow(dormId: String): Flow<List<Wg>> {
        return mFirestore
            .collection(Constants.WG).whereEqualTo("dormId", dormId)
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getWgFromSnapshot(it)
                } ?: listOf()
            }
    }

    // Parses the document snapshot to the desired object
    fun getWgFromSnapshot(documentSnapshot: DocumentSnapshot): Wg {
        return documentSnapshot.toObject(Wg::class.java)!!.let {
            it.id = documentSnapshot.id
            return@let it
        }
    }
}