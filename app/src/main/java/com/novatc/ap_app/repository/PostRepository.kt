package com.novatc.ap_app.repository

import com.google.firebase.firestore.DocumentReference
import com.novatc.ap_app.firestore.PostFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception
import javax.inject.Inject

class PostRepository
@Inject constructor(
    private val postFirestore: PostFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun addPost(headline: String, text: String, keyword: String, date: String) {
        val userId = userFirestore.getCurrentUserID() ?: throw Exception("No user id, when trying to add a post.")
        val userName = userFirestore.getUserData(userId)!!.username
        val post = Post(headline, text, keyword, userName, date, userId)
        postFirestore.addPost(post)
    }
    @ExperimentalCoroutinesApi
    fun getPostsAsFlow(): kotlinx.coroutines.flow.Flow<List<Post>> {
        return postFirestore.getPostsAsFlow()
    }

    suspend fun getPosts(): ArrayList<Post> {
        return postFirestore.getPosts()
    }

    suspend fun addComment(postID: String, comment: Comment): DocumentReference? {
        return postFirestore.addComment(postID, comment = comment)
    }

    suspend fun deletePost(postID: String) {
        postFirestore.deletePost(postID)
    }

    suspend fun getUserPosts(): ArrayList<Post> {
        val userId = userFirestore.getCurrentUserID() ?: throw Exception("No user id, when trying to get user posts.")
        return postFirestore.getUserPosts(userId)
    }
}