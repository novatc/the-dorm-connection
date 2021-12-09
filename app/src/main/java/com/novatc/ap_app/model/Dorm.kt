package com.novatc.ap_app.model


data class Dorm(
    val name: String = "",
    val address: String = "",
    val description: String = "",
    var id: String = ""

    ) {
    val userList: ArrayList<User> = ArrayList()
    val roomList: ArrayList<Room> = ArrayList()

    fun addUserToDorm(user: User) {
        userList.add(user)
    }
    fun addRoomToDorm(room:Room){
        roomList.add(room)
    }
}
