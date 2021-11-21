package model

data class Event(
                 val name: String = "",
                 val date: String  = "",
                 val author: String  = "",
                 val text: String  = "",
                 val fcmToken: String  = ""
)
