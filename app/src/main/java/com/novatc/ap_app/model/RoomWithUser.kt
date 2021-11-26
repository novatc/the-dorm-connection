package com.novatc.ap_app.model

data class RoomWithUser(
    val name: String = "",
    val address: String = "",
    val text: String = "",
    val minimumBookingTime: String = "",
    var user: User? = null
)
