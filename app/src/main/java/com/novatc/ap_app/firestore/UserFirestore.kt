package com.novatc.ap_app.firestore

import com.novatc.ap_app.constants.Constants
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.model.User
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserFirestore @Inject constructor() {

    private val mFirestore = Firebase.firestore

    fun getCurrentUserID(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getCurrentUserMail(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user!!.email
    }

    suspend fun signUp(name: String, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        mFirestore.collection(Constants.USER)
            .document(getCurrentUserID()!!)
            .set(User(getCurrentUserID()!!, name, email))
            .await()
    }

    suspend fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    suspend fun deleteUser() {
        val user = Firebase.auth.currentUser ?: throw Exception("No current user, when deleting user.")
        user.delete().await()
        mFirestore.collection(Constants.USER).document(user.uid).delete().await()
    }

    suspend fun getUserData(uid: String): User? {
        val snapshot = mFirestore.collection(Constants.USER).document(uid).get().await()
        if (snapshot.data == null) return null
        return snapshot.toObject<User>()
    }

}
