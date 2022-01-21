package com.novatc.ap_app.firestore

import com.novatc.ap_app.constants.Constants
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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


class UserFirestore @Inject constructor(
    private val helperFirestore: HelperFirestore
) {

    private val mFirestore = Firebase.firestore

    fun getCurrentUserID(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getCurrentUserMail(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user!!.email
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
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
        val user =
            Firebase.auth.currentUser ?: throw Exception("No current user, when deleting user.")
        val userId = user.uid

        // Delete all posts by the user
        val userPostsSnapshot =
            mFirestore.collection(Constants.POST).whereEqualTo("creatorID", userId).get().await()
        for (post in userPostsSnapshot.documents) {
            helperFirestore.deleteCollection(post.reference.collection(Constants.COMMENTS), 100)
            post.reference.delete().await()
        }
        // Delete all comments by the user
        val postsSnapshot =
            mFirestore.collection(Constants.POST).get().await()
        for (post in postsSnapshot.documents) {
            val postComments = post.reference.collection(Constants.COMMENTS).whereEqualTo("authorID", userId).get().await()
            for (postComment in postComments.documents)
                postComment.reference.delete().await()
        }
        // Delete all user events
        val eventsSnapshot = mFirestore.collection(Constants.EVENTS).whereEqualTo("authorId", userId).get().await()
        for (event in eventsSnapshot.documents) {
            helperFirestore.deleteCollection(event.reference.collection(Constants.ATTENDEES), 100)
            event.reference.delete().await()
        }
        // Delete user from firebase auth and from firebase db
        user.delete().await()
        mFirestore.collection(Constants.USER).document(user.uid).delete().await()

    }

    suspend fun getUserData(uid: String): User? {
        val snapshot = mFirestore.collection(Constants.USER).document(uid).get().await()
        if (snapshot.data == null) return null
        return snapshot.toObject<User>()
    }


    fun updateUserDorm(user: User) {
        val dbUser = mFirestore.collection(Constants.USER).document(user.id)
        dbUser.update("userDorm", user.userDorm)
            .addOnSuccessListener { Log.d("FIRE", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w("FIRE", "Error updating document", e) }

    }

    suspend fun updateUserWg(userId: String, wgId: String, wgName: String) {
        mFirestore.collection(Constants.USER).document(userId)
            .update("userWgId", wgId, "userWgName", wgName).await()
    }

    suspend fun updateUserName(userId: String, userName: String) {
        mFirestore.collection(Constants.USER).document(userId).update("username", userName).await()
    }

    suspend fun updateUserPassword(user: FirebaseUser, currentPassword: String, newPassword: String) {
        val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
        user.reauthenticate(credential).await()
        user.updatePassword(newPassword)
    }

    @ExperimentalCoroutinesApi
    fun <T> CollectionReference.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
        return getQuerySnapshotFlow()
            .map { snapshot ->
                return@map mapper(snapshot)
            }
    }

    // Parses the document snapshot to the desired object
    fun getUserFromSnapshot(documentSnapshot: DocumentSnapshot): User {
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
                Log.e("", "cancelling the listener on collection at path - $path")
                listenerRegistration.remove()
            }
        }
    }

}
