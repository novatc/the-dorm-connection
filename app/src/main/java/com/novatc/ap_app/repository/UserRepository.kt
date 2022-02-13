package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.User
import java.lang.Exception
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userFirestore: UserFirestore
) {
    fun readCurrentId(): String? {
        return userFirestore.getCurrentUserID()
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

    suspend fun delete(password: String) {
        return userFirestore.deleteUser(password)
    }

    suspend fun readCurrent(): User? {
        val currentUserId = readCurrentId() ?: return null
        return userFirestore.getUserData(currentUserId)
    }

    fun updateUserWithDorm(user: User){
        return userFirestore.updateUserDorm(user)
    }

    suspend fun updateUserName(userName: String) {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to update user name")
        userFirestore.updateUserName(userId, userName)
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String) {
        val user = userFirestore.getCurrentUser()
            ?: throw Exception("No user, when trying to update user password")
        userFirestore.updateUserPassword(user, currentPassword, newPassword)
    }
}