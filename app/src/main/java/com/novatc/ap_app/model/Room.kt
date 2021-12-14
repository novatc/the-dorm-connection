package com.novatc.ap_app.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Room(
    val name: String? = "",
    val address: String? = "",
    val userId: String? = "",
    val text: String? = "",
    val minimumBookingTime: String? = "",
    var imageName: String? = "",
    var key: String? = "",

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        )


    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(name)
        parcel.writeString(text)
        parcel.writeString(address)
        parcel.writeString(userId)
        parcel.writeString(text)
        parcel.writeString(minimumBookingTime)
        parcel.writeString(imageName)
        parcel.writeString(key)
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