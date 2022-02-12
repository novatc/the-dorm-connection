package com.novatc.ap_app.model

data class Comment(
    var id: String ="",
    var content: String = "",
    var authorID: String = "",
    var authorName: String = "",
    val date: Long? = 0

)

