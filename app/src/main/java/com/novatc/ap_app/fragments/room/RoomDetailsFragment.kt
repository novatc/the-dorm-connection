package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.RoomDetailsStateAdapter
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.repository.UserRepository
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_room_details.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomDetailsFragment : Fragment() {
    private lateinit var roomDetailsStateAdapter: RoomDetailsStateAdapter
    private lateinit var viewPager: ViewPager2
    private val tabNames = listOf(
        R.string.detail_room_general,
        R.string.detail_room_location,
        R.string.detail_room_book
    )
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

        view.et_room_name.text = room.name
        roomDetailsViewModel.setRoom(room)
        roomDetailsViewModel.loadRoomImage()
        roomDetailsViewModel.loadImageRequest.observe(this, { request ->
            if (request.status == Request.Status.SUCCESS) {
                val imageUri = request.data
                Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .placeholder(R.drawable.circular_progress_bar)
                    .into(view.iv_room_image)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomDetailsStateAdapter = RoomDetailsStateAdapter(this)
        viewPager = view.room_details_pager
        viewPager.adapter = roomDetailsStateAdapter
        val tabLayout = view.room_details_tabs
        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab, position -> tab.setText(tabNames[position]) }.attach()
    }

}