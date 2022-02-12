package com.novatc.ap_app.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude

data class Room(
    val name: String? = "",
    val streetName: String? = "",
    val houseNumber: String? = "",
    val city: String? = "",
    val shortDescription: String? = "",
    val description: String? = "",
    val minimumBookingTime: String? = "",
    val maximumBookingTime: String? = "",
    val creatorID: String? = "",
    val dormId: String? = "",
    @get:Exclude var id: String? = "",
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
        )


    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(name)
        parcel.writeString(streetName)
        parcel.writeString(houseNumber)
        parcel.writeString(city)
        parcel.writeString(description)
        parcel.writeString(shortDescription)
        parcel.writeString(minimumBookingTime)
        parcel.writeString(maximumBookingTime)
        parcel.writeString(creatorID)
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