package com.novatc.ap_app.model

data class User(
    var id: String = "",
    val username: String = "",
    val mail: String = "",
    var userDorm: String = "",
    var userDormID: String = "",
    var userWgName: String = "",
    var userWgId: String = ""
)