package com.novatc.ap_app.model

import android.text.Editable

data class Comment(
    var id: String ="",
    var content: Editable = "",
    var author: String = ""
)
