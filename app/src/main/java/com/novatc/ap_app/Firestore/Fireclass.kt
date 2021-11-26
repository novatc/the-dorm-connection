package com.novatc.ap_app.Firestore

import com.novatc.ap_app.Constants.Constants
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
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


class Fireclass {

    private val mFirestore = Firebase.firestore

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getCurrentUser(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val name = user.email
        return name
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

    @ExperimentalCoroutinesApi
    fun getPostsAsFlow(): Flow<ArrayList<Post>> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection(Constants.POST)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException?
                    ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching posts",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { it.toObject(Post::class.java) }
                    if (map != null) {
                        offer(map as ArrayList<Post>)
                    }
                }
            awaitClose {
                Log.d("POSTS", "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        val snapshot = mFirestore.collection(Constants.POST).get().await()
        for (post in snapshot) {
            post.toObject(Post::class.java).let {
                posts.add(it)
            }
        }
        return posts

    }

    suspend fun getUserPosts(userID: String): ArrayList<Post> {
        val posts = ArrayList<Post>()
        val snapshot = mFirestore.collection(Constants.POST).whereEqualTo("creatorID", userID).get().await()
        for (post in snapshot) {
            post.toObject(Post::class.java).let {
                posts.add(it)
            }
        }
        return posts

    }

    fun addPost(post: Post) {
        mFirestore.collection(Constants.POST).document().set(post, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("POST", "Post saved to DB")
            }.addOnFailureListener { e ->
                Log.e("POST", "Error while saving: $e")
            }
    }

    suspend fun getUserData(uid: String): User? {
        val snapshot = mFirestore.collection(Constants.USER).document(uid).get().await()
        val user = snapshot.toObject<User>()
        Log.e("FIRE", user!!.username)
        return user
    }

    fun addEvent(event: Event) {
        mFirestore.collection(Constants.EVENTS).document().set(event, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("EVENT", "Event saved to DB")
            }.addOnFailureListener { e ->
                Log.e("EVENT", "Error while saving: $e")
            }
    }

    fun addRoomToDD(room: Room) {
        mFirestore.collection(Constants.ROOMS).document().set(room, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("ROOM", "Room saved to DB")
            }.addOnFailureListener { e ->
                Log.e("ROOM", "Error while saving: $e")
            }
    }
}
