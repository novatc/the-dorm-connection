package com.novatc.ap_app.model

import android.os.Parcel
import android.os.Parcelable

data class EventWithUser(
    val name: String? = "",
    val date: String? = "",
    val text: String? = "",
    val streetName: String? = "",
    val houseNumber: String? = "",
    val city: String? = "",
    var user: User? = null,
    val userList: ArrayList<User> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(name)
        parcel.writeString(text)
        parcel.writeString(date)
        parcel.writeString(date)
        parcel.writeString(streetName)
        parcel.writeString(houseNumber)
        parcel.writeString(city)

    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}
