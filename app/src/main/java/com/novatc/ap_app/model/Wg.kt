package com.novatc.ap_app.model

import com.google.firebase.firestore.Exclude

data class Wg(
    @get:Exclude var id: String = "",
    val name: String = "",
    val slogan: String = "",
    val dormId: String? = "",
)
