package com.novatc.ap_app.viewModels

import com.novatc.ap_app.constants.Constants
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.novatc.ap_app.fragments.*
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.model.RoomWithUser
import com.novatc.ap_app.model.User
import com.novatc.ap_app.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    roomRepository: RoomRepository
): ViewModel() {

    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val TAG = "RoomViewModel"
    private var _rooms: MutableLiveData<ArrayList<RoomWithUser>> = MutableLiveData()

    init {
        loadRooms()
    }

    internal var rooms: MutableLiveData<ArrayList<RoomWithUser>>
        get() {
            return _rooms
        }
        set(value) {
            _rooms = value
        }

    private fun loadRooms() {
        fireStore.collection(Constants.ROOMS).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen for rooms failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val documents = snapshot.documents
                GlobalScope.launch(Dispatchers.IO) {
                    val allRooms = ArrayList<RoomWithUser>()
                    documents.forEach {
                        val room = it.toObject(Room::class.java)
                        if (room != null) {
                            val user = fireStore.collection(Constants.USER)
                                .document(room.userId)
                                .get()
                                .await()
                                .toObject(User::class.java)
                            val roomWithUser =
                                RoomWithUser(room.name, room.address, room.text, room.minimumBookingTime, user)
                                allRooms.add(roomWithUser)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        _rooms.value = allRooms
                    }
                }


            }
        }

    }


}