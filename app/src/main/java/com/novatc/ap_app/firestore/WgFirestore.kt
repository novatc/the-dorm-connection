package com.novatc.ap_app.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Wg
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WgFirestore @Inject constructor() {

    private val mFirestore = Firebase.firestore

    suspend fun createWg(wg: Wg) {
        mFirestore.collection(Constants.WG).add(wg).await()
    }
}