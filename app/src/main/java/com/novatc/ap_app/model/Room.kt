package com.novatc.ap_app.model

import android.net.Uri

data class Room(
    val name: String = "",
    val address: String = "",
    val userId: String = "",
    val text: String = "",
    val minimumBookingTime: String = "",
    var imageName: String = "",
)