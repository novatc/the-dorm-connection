package com.novatc.ap_app.firestore

import com.novatc.ap_app.constants.Constants
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.activities.SignInActivity
import com.novatc.ap_app.activities.SignUpActivity
import com.google.firebase.firestore.ktx.toObject
import com.novatc.ap_app.model.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UserFirestore @Inject constructor() {

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
    suspend fun getUserDataAsFlow(): Flow<User>{
        return mFirestore.collection(Constants.USER).getDataFlow {
            querySnapshot ->
            querySnapshot?.toObjects(User::class.java)
            (querySnapshot?.documents?.map {
                getUserFromSnapshot(it)
            }?: User) as User
        }
    }

    fun updateUserDorm(user: User) {
        val dbUser = mFirestore.collection(Constants.USER).document(user.id)
        dbUser.update("userDorm", user.userDorm)
            .addOnSuccessListener { Log.d("FIRE", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("FIRE", "Error updating document", e) }

    }

    @ExperimentalCoroutinesApi
    fun <T> CollectionReference.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
        return getQuerySnapshotFlow()
            .map { snapshot ->
                return@map mapper(snapshot)
            }
    }

    // Parses the document snapshot to the desired object
    fun getUserFromSnapshot(documentSnapshot: DocumentSnapshot) : User {
        return documentSnapshot.toObject(User::class.java)!!.let {
            return@let it
        }

    }

    @ExperimentalCoroutinesApi
    fun CollectionReference.getQuerySnapshotFlow(): Flow<QuerySnapshot?> {
        return callbackFlow {
            val listenerRegistration =
                addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("Snapshot Flow", "Error fetchting collect = $path")
                        return@addSnapshotListener
                    }
                    trySend(querySnapshot).isSuccess
                }
            awaitClose {
                Log.e("","cancelling the listener on collection at path - $path")
                listenerRegistration.remove()
            }
        }
    }

}
