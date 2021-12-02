package com.novatc.ap_app.repository

import com.novatc.ap_app.activities.SignInActivity
import com.novatc.ap_app.activities.SignUpActivity
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.User
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userFirestore: UserFirestore
) {
    private fun readCurrentId(): String {
        return userFirestore.getCurrentUserID()
    }

    fun readCurrentFirebaseUser(): String? {
        return userFirestore.getCurrentUserMail()
    }

    fun register(activity: SignUpActivity, userInfo: User) {
        return userFirestore.registerUser(activity, userInfo)
    }

    fun login(activity: SignInActivity) {
        return userFirestore.signInUser(activity)
    }

    fun logout() {
        return userFirestore.logout()
    }

    fun delete() {
        return userFirestore.deleteUser()
    }

    suspend fun readCurrent(): User? {
        val currentUserId = readCurrentId()
        return userFirestore.getUserData(currentUserId)
    }

    suspend fun read(uid: String): User? {
        return userFirestore.getUserData(uid)
    }
}