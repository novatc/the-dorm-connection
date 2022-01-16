package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.firestore.WgFirestore
import com.novatc.ap_app.model.Wg
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WgRepository @Inject constructor(
    private val wgFirestore: WgFirestore,
    private val userFirestore: UserFirestore
){

    suspend fun getWg(wgId: String): Wg {
       return wgFirestore.getWg(wgId)
    }
    suspend fun createWg(name: String, slogan: String) {
        wgFirestore.createWg(Wg(name = name, slogan = slogan))
    }

    suspend fun joinWg(wgId: String) {
        val wg = wgFirestore.getWg(wgId)
        val userId = userFirestore.getCurrentUserID()!!
        userFirestore.updateUserWg(userId, wg.id, wg.name)
        val user = userFirestore.getUserData(userId)
        wgFirestore.joinWg(wgId, user!!)
    }

    suspend fun leaveWg() {
        val user = userFirestore.getUserData(userFirestore.getCurrentUserID()!!)
        if (user!!.userWgId != "") {
            userFirestore.updateUserWg(user.id, "", "")
            wgFirestore.leaveWg(user.userWgId, user.id)
        }

    }

    @ExperimentalCoroutinesApi
    fun getWgs(): Flow<List<Wg>> {
        return wgFirestore.getWgsFlow()
    }
}