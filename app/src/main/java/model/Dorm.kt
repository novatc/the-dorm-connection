package model

data class Dorm(
    val id: String = "",
    val name: String = "",
    val address: String ="",
    val roomList: ArrayList<Room>,
    val userList: ArrayList<User>
)
