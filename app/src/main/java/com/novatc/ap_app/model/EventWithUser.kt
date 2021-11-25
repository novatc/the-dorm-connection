package com.novatc.ap_app.model

data class EventWithUser(
    val name: String = "",
    val date: String = "",
    val text: String = "",
    var user: User? = null
)