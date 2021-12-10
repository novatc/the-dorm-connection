package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.User
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userFirestore: UserFirestore
) {
    fun readCurrentId(): String? {
        return userFirestore.getCurrentUserID()
    }

    fun readCurrentFirebaseUser(): String? {
        return userFirestore.getCurrentUserMail()
    }

    suspend fun signUp(name: String, email: String, password: String) {
        return userFirestore.signUp(name, email, password)
    }

    suspend fun login(email: String, password: String) {
        return userFirestore.login(email, password)
    }

    fun logout() {
        return userFirestore.logout()
    }

    suspend fun delete() {
        return userFirestore.deleteUser()
    }

    suspend fun readCurrent(): User? {
        val currentUserId = readCurrentId() ?: return null
        return userFirestore.getUserData(currentUserId)
    }

    suspend fun read(uid: String): User? {
        return userFirestore.getUserData(uid)
    }
}