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
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
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

    suspend fun deleteRoom(roomID: String) {
        roomRepository.deleteRoom(roomID)
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
                            val user = room.userId?.let { it1 ->
                                fireStore.collection(Constants.USER)
                                    .document(it1)
                                    .get()
                                    .await()
                                    .toObject(User::class.java)
                            }
                            val roomWithUser =
                                room.name?.let { it1 -> room.address?.let { it2 ->
                                    room.text?.let { it3 ->
                                        room.minimumBookingTime?.let { it4 ->
                                            RoomWithUser(it1,
                                                it2, it3, it4, user)
                                        }
                                    }
                                } }
                            if (roomWithUser != null) {
                                allRooms.add(roomWithUser)
                            }
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