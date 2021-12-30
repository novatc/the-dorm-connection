package com.novatc.ap_app.firestore

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HelperFirestore @Inject constructor(){

    suspend fun deleteCollection(collection: CollectionReference, batchSize: Long) {
        val snapshot = collection.limit(batchSize).get().await()
        var deleted = 0
        val documents = snapshot.documents
        for (document in documents) {
            document.reference.delete().await()
            deleted++
        }
        if (deleted >= batchSize) {
            deleteCollection(collection, batchSize)
        }
    }
}