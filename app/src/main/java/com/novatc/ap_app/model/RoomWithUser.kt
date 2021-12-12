package com.novatc.ap_app.model

import android.os.Parcel
import android.os.Parcelable

data class RoomWithUser(
    val name: String? = "",
    val address: String? = "",
    val text: String? = "",
    val minimumBookingTime: String? = "",
    var user: User? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(text)
        parcel.writeString(minimumBookingTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomWithUser> {
        override fun createFromParcel(parcel: Parcel): RoomWithUser {
            return RoomWithUser(parcel)
        }

        override fun newArray(size: Int): Array<RoomWithUser?> {
            return arrayOfNulls(size)
        }
    }
}
