package com.novatc.ap_app.firestore

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Dorm
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DormFirestore @Inject constructor(
    private val userFirestore: UserFirestore
) {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun addDorm(dorm:Dorm):Task<Void>{
        return mFirestore.collection(Constants.DORMS).document().set(dorm, SetOptions.merge())
    }

    fun getDormsAsFlow(): Flow<List<Dorm>> {
        return mFirestore.collection(Constants.DORMS).getDataFlow { querySnapshot ->
            querySnapshot?.documents?.map {
                getDormFromSnapshot(it)
            } ?: listOf()
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
    fun getDormFromSnapshot(documentSnapshot: DocumentSnapshot) : Dorm {
        return documentSnapshot.toObject(Dorm::class.java)!!.let {
            Log.e("FIRE", "Dorm from DB: ${it}")

            it.id = documentSnapshot.id
            return@let it
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
                Log.e("","cancelling the listener on collection at path - $path")
                listenerRegistration.remove()
            }
        }
    }
}