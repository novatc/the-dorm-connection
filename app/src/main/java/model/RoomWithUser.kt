package model

data class RoomWithUser(
    val name: String = "",
    val address: String = "",
    val text: String = "",
    var user: User? = null
)
