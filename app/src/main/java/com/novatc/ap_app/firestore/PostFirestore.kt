package com.novatc.ap_app.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostFirestore @Inject constructor(){
    private val mFirestore = Firebase.firestore

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
}