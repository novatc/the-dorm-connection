package com.novatc.ap_app.repository

import com.google.firebase.firestore.DocumentReference
import com.novatc.ap_app.firestore.PostFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.lang.Exception
import javax.inject.Inject

class PostRepository
@Inject constructor(
    private val postFirestore: PostFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun addPost(headline: String, text: String, keyword: String, date: Long) {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to add a post.")
        val user = userFirestore.getUserData(userId)
        val post = Post(headline, text, keyword, user!!.username, date, userId, user.userDormID)
        postFirestore.addPost(post)
    }

    @ExperimentalCoroutinesApi
    suspend fun getPostsAsFlow(): Flow<List<Post>> {
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying fetch posts")
        val userDormId = userFirestore.getUserData(userId)!!.userDormID
        return postFirestore.getPostsAsFlow(userDormId)
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
        val userId = userFirestore.getCurrentUserID()
            ?: throw Exception("No user id, when trying to get user posts.")
        return postFirestore.getUserPosts(userId)
    }
    @ExperimentalCoroutinesApi
    suspend fun getCommentsAsFlow(postID: String): Flow<List<Comment>> {
        return postFirestore.getCommentsFlow(postID)
    }

    suspend fun deleteComment(commentID: String, postID: String) {
        postFirestore.deleteComment(commentID, postID)
    }
}