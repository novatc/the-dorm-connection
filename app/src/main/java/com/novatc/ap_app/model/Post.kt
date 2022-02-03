package com.novatc.ap_app.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude


data class Post(
    val headline: String? = "",
    val text: String? = "",
    val keyword: String? = "",
    val creator: String? = "",
    val date: String? = "",
    val creatorID: String? = "",
    val dormId: String? = "",
    @get: Exclude var id: String? = "",

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )



    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(headline)
        parcel.writeString(text)
        parcel.writeString(keyword)
        parcel.writeString(creator)
        parcel.writeString(date)
        parcel.writeString(creatorID)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}

