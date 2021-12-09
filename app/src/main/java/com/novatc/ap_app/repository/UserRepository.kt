package com.novatc.ap_app.repository

import com.novatc.ap_app.activities.SignInActivity
import com.novatc.ap_app.activities.SignUpActivity
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userFirestore: UserFirestore
) {
    fun readCurrentId(): String {
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
    suspend fun readCurrentUserAsFlow(): Flow<User> {
        return userFirestore.getUserDataAsFlow()
    }

    suspend fun read(uid: String): User? {
        return userFirestore.getUserData(uid)
    }
    fun updateUserWithDorm(user: User){
        return userFirestore.updateUserDorm(user)
    }
}