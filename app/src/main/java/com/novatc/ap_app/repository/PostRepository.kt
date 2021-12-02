package com.novatc.ap_app.repository

import com.novatc.ap_app.firestore.PostFirestore
import com.novatc.ap_app.firestore.UserFirestore
import com.novatc.ap_app.model.Post
import javax.inject.Inject

class PostRepository
@Inject constructor(
    private val postFirestore: PostFirestore,
    private val userFirestore: UserFirestore
) {

    suspend fun addPost(headline: String, text: String, keyword: String, date: String) {
        val userId = userFirestore.getCurrentUserID()
        val userName = userFirestore.getUserData(userId)!!.username
        val post = Post(headline, text, keyword, userName, date, userId)
        postFirestore.addPost(post)
    }

    suspend fun getPosts(): ArrayList<Post> {
        return postFirestore.getPosts()
    }

    suspend fun getUserPosts(): ArrayList<Post> {
        val userId = userFirestore.getCurrentUserID()
        return postFirestore.getUserPosts(userId)
    }
}