package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.DormFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Dorm
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DormRepository @Inject constructor(
    private val dormFirestore: DormFirestore,
    private val userFirestore: UserFirestore
) {
    suspend fun add(dormName: String, dormDescription: String, dormAddress: String){
        val user = userFirestore.getUserData(userFirestore.getCurrentUserID())!!
        val dorm = Dorm(name = dormName, address = dormAddress, description = dormDescription)
        dorm.addUserToDorm(user)
        return withContext(Dispatchers.IO) {
            dormFirestore.addDorm(dorm).await()
        }
    }

    @ExperimentalCoroutinesApi
    fun getPostsAsFlow(): Flow<List<Dorm>> {
        return dormFirestore.getDormsAsFlow()
    }
}