package com.novatc.ap_app.firestore

import android.util.Log
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostFirestore @Inject constructor(
    private val helperFirestore: HelperFirestore) {
    private val mFirestore = Firebase.firestore


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

    @ExperimentalCoroutinesApi
    fun <T> CollectionReference.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T> {
        return getQuerySnapshotFlow()
            .map { snapshot ->
                return@map mapper(snapshot)
            }
    }

    @ExperimentalCoroutinesApi
    fun getPostsAsFlow(): Flow<List<Post>> {
        return mFirestore
            .collection(Constants.POST)
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getPostFromSnapshot(it)
                } ?: listOf()
            }
    }

    suspend fun getPosts(): ArrayList<Post> {
        val posts = ArrayList<Post>()
        val snapshot = mFirestore.collection(Constants.POST).get().await()
        for (post in snapshot) {
            post.toObject(Post::class.java).let {
                it.key = post.id
                posts.add(it)
            }
        }
        return posts

    }

    suspend fun getUserPosts(userID: String): ArrayList<Post> {
        val posts = ArrayList<Post>()
        val snapshot =
            mFirestore.collection(Constants.POST).whereEqualTo("creatorID", userID).get().await()
        for (post in snapshot) {
            post.toObject(Post::class.java).let {
                it.key = post.id
                posts.add(it)
            }
        }
        return posts

    }

    suspend fun addPost(post: Post) {
        val ref = mFirestore.collection(Constants.POST).add(post).await()
        mFirestore.collection(Constants.POST).document().collection(Constants.COMMENTS)
        post.key = ref.id
        Log.e("FIRE", "Created local Post with id: ${post.key}")
    }

    suspend fun deletePost(postID: String) {
        val comments =
            mFirestore.collection(Constants.POST).document(postID).collection(Constants.COMMENTS)
        helperFirestore.deleteCollection(comments, 5)
        mFirestore.collection(Constants.POST).document(postID).delete().await()

    }

    // Parses the document snapshot to the desired object
    fun getPostFromSnapshot(documentSnapshot: DocumentSnapshot): Post {
        return documentSnapshot.toObject(Post::class.java)!!.let {
            it.key = documentSnapshot.id
            return@let it
        }

    }

    suspend fun addComment(postID: String, comment: Comment): DocumentReference? {
        return mFirestore.collection(Constants.POST).document(postID).collection(Constants.COMMENTS)
            .add(comment).await()
    }

    fun getCommentsFlow(postID: String): Flow<List<Comment>> {
        return mFirestore.collection(Constants.POST).document(postID).collection(Constants.COMMENTS)
            .getDataFlow { querySnapshot ->
                querySnapshot?.documents?.map {
                    getCommentsFromSnapshot(it)
                } ?: listOf()
            }

    }

    fun getCommentsFromSnapshot(documentSnapshot: DocumentSnapshot): Comment {
        return documentSnapshot.toObject(Comment::class.java)!!.let {
            it.id = documentSnapshot.id
            return@let it
        }
    }

    suspend fun deleteComment(commentID: String, postID: String) {
        mFirestore.collection(Constants.POST).document(postID).collection(Constants.COMMENTS).document(commentID).delete().await()
    }


}