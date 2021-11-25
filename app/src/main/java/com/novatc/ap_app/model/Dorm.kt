package com.novatc.ap_app.model

data class Dorm(
    val id: String = "",
    val name: String = "",
    val address: String ="",
    val roomList: ArrayList<Room>,
    val userList: ArrayList<User>
)
