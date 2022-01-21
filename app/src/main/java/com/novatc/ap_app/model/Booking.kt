package com.novatc.ap_app.model

data class Booking(
    var id: String ="",
    var userID: String ="",
    var startingDate: Long = 0L,
    var endDate: Long = 0L
)