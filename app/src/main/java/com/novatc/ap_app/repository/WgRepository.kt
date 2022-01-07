package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.WgFirestore
import com.novatc.ap_app.model.Wg
import javax.inject.Inject

class WgRepository @Inject constructor(
    private val wgFirestore: WgFirestore
){

    suspend fun createWg(name: String, slogan: String) {
        wgFirestore.createWg(Wg(name, slogan))
    }
}