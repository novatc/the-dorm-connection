package com.novatc.ap_app.model

import android.os.Parcel
import android.os.Parcelable

data class EventWithUser(
    val id: String? = "",
    val name: String? = "",
    val date: String? = "",
    val text: String? = "",
    val streetName: String? = "",
    val houseNumber: String? = "",
    val city: String? = "",
    var user: User? = User(),
    var userList: ArrayList<User> = ArrayList()
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
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeString(text)
        parcel.writeString(date)
        parcel.writeString(date)
        parcel.writeString(streetName)
        parcel.writeString(houseNumber)
        parcel.writeString(city)

    }

    companion object CREATOR : Parcelable.Creator<EventWithUser> {
        override fun createFromParcel(parcel: Parcel): EventWithUser {
            return EventWithUser(parcel)
        }

        override fun newArray(size: Int): Array<EventWithUser?> {
            return arrayOfNulls(size)
        }
    }
    fun addUserList(user: ArrayList<User>){
        userList = user
    }
}
