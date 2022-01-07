package com.novatc.ap_app.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

data class Room(
    val name: String? = "",
    val address: String? = "",
    val description: String? = "",
    val minimumBookingTime: String? = "",
    val creatorID: String? = "",
    var key: String? = "",
    @get:Exclude var id: String? = "",
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        )


    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(description)
        parcel.writeString(minimumBookingTime)
        parcel.writeString(creatorID)
        parcel.writeString(key)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}