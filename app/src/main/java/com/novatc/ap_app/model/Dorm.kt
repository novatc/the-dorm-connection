package com.novatc.ap_app.model

import android.os.Parcel
import android.os.Parcelable


data class Dorm(
    val name: String? = "",
    val address: String? = "",
    val description: String? = "",
    var id: String? = "",
    val userList: ArrayList<User> = ArrayList(),
    val roomList: ArrayList<Room> = ArrayList()

    ): Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    )

    fun addUserToDorm(user: User) {
        userList.add(user)
    }
    fun addRoomToDorm(room:Room){
        roomList.add(room)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(description)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dorm> {
        override fun createFromParcel(parcel: Parcel): Dorm {
            return Dorm(parcel)
        }

        override fun newArray(size: Int): Array<Dorm?> {
            return arrayOfNulls(size)
        }
    }

}
