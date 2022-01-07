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
import com.bumptech.glide.Glide
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.repository.UserRepository
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event_details.view.*
import kotlinx.android.synthetic.main.fragment_room_details.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomDetailsFragment : Fragment() {
    private val args by navArgs<RoomDetailsFragmentArgs>()
    @Inject
    lateinit var userRepository: UserRepository
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
        view.detail_room_description.text = room.description
        roomDetailsViewModel.setRoom(room)
        roomDetailsViewModel.loadRoomImage()
        roomDetailsViewModel.loadImageRequest.observe(this, { request ->
            if (request.status == Request.Status.SUCCESS) {
                val imageUri = request.data
                Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .placeholder(R.drawable.circular_progress_bar)
                    .into(view.iv_room_picture)
            }
        })
        if (room.creatorID == userRepository.readCurrentId()){
            view.btn_delete_room.visibility = View.VISIBLE
            view.btn_delete_room.setOnClickListener {
                lifecycleScope.launch {
                    room.id?.let { it1 ->
                        roomDetailsViewModel.deleteRoom(roomID = it1)
                        }
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