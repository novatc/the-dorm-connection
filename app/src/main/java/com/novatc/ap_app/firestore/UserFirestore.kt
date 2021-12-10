package com.novatc.ap_app.firestore

import com.novatc.ap_app.constants.Constants
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.model.User
import com.google.firebase.firestore.ktx.toObject
import com.novatc.ap_app.model.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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

//    @ExperimentalCoroutinesApi
//    suspend fun getUserDataAsFlow(): Flow<User>{
//        return mFirestore.collection(Constants.USER).getDataFlow<User> {
//            querySnapshot ->
//            querySnapshot?.toObjects(User::class.java)
//            (querySnapshot?.documents?.map {
//                getUserFromSnapshot(it)
//            }
//        }
//    }

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
        return documentSnapshot.toObject(User::class.java)!!

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
