package com.novatc.ap_app.firestore

import android.util.Log
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novatc.ap_app.constants.Constants
import com.novatc.ap_app.model.Event
import javax.inject.Inject

class EventFirestore @Inject constructor(){
    private val mFirestore = Firebase.firestore

    fun addEvent(event: Event) {
        mFirestore.collection(Constants.EVENTS).document().set(event, SetOptions.merge())
            .addOnSuccessListener { document ->
                Log.e("EVENT", "Event saved to DB")
            }.addOnFailureListener { e ->
                Log.e("EVENT", "Error while saving: $e")
            }
    }
}