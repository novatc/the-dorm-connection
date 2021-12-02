package com.novatc.ap_app.firestore

import com.novatc.ap_app.constants.Constants
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.activities.SignInActivity
import com.novatc.ap_app.activities.SignUpActivity
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.model.User
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class UserFirestore @Inject constructor(){

    private val mFirestore = Firebase.firestore

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getCurrentUserMail(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user!!.email
    }

    fun registerUser(activity: SignUpActivity, userInfo: User) {

        mFirestore.collection(Constants.USER).document(getCurrentUserID())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error in Firestore (Register) ", e)
            }
    }

    fun signInUser(activity: SignInActivity) {
        mFirestore.collection(Constants.USER).document(getCurrentUserID()).get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                activity.signInSuccess(loggedInUser!!)
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error in Firestore (LogIn) ", e)
            }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun deleteUser() {
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("PROFILE", "User account deleted.")
                }
            }
    }

    suspend fun getUserData(uid: String): User? {
        val snapshot = mFirestore.collection(Constants.USER).document(uid).get().await()
        val user = snapshot.toObject<User>()
        Log.e("FIRE", user!!.username)
        return user
    }

}
