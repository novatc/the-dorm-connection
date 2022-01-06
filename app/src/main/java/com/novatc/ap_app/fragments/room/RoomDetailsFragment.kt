package com.novatc.ap_app.fragments.room

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.repository.UserRepository
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_room_details.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomDetailsFragment : Fragment() {
    private val args by navArgs<RoomDetailsFragmentArgs>()
    @Inject
    lateinit var  userRepository: UserRepository
    private val roomDetailsViewModel: RoomDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_details, container, false)
        val room: Room = args.clickedRoom
        Log.e("FIRE", "Post viewed: ${room}")

        view.detail_room_title.text = room.name
        view.detail_room_description.text = room.text
        val context : Context? = context
        lifecycleScope.launch{
            roomDetailsViewModel.loadPicture(view.iv_room_picture, room.imageName, context)
        }

        if (room.creatorID == userRepository.readCurrentId()){
            view.btn_delete_room.visibility = View.VISIBLE
            view.btn_delete_room.setOnClickListener {
                lifecycleScope.launch {
                    room.id?.let { it1 ->
                        room.imageName?.let { it2 ->
                            roomDetailsViewModel.deleteRoom(roomID = it1, imageUri = it2)
                        }}
                }
                val action = RoomDetailsFragmentDirections.actionRoomDetailsFragmentToFragmentRooms()
                findNavController().navigate(action)
            }
        }else{
            view.btn_delete_room.visibility = View.GONE
        }

        return view
    }

}