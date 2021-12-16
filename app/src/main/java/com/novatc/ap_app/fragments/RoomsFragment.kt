package com.novatc.ap_app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.RoomsAdapter
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.model.RoomWithUser
import com.novatc.ap_app.viewModels.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_post.view.*
import kotlinx.android.synthetic.main.fragment_event.view.*
import kotlinx.android.synthetic.main.fragment_room_list.view.*

@AndroidEntryPoint
class RoomsFragment : Fragment(), RoomsAdapter.OnItemClickListener {
    var roomList:ArrayList<RoomWithUser> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)
        fillRoomsList(view)
        setAddRoomButtonListener(view)
        return view
    }

    override fun onItemClick(position: Int) {
        val room = roomList[position]
        val action = RoomsFragmentDirections.actionFragmentRoomsToRoomDetailsFragment(room)
        findNavController().navigate(action)
    }

    private fun fillRoomsList(view: View) {
        view.roomsListSpinner.visibility = View.VISIBLE
        val recyclerView: RecyclerView = view.findViewById((R.id.available_rooms))
        val model: RoomViewModel by viewModels()
        model.rooms.observe(this, { rooms ->
            roomList = rooms
            view.roomsListSpinner.visibility = View.GONE
            recyclerView.adapter = RoomsAdapter(rooms, this)
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun setAddRoomButtonListener(view: View) {
        val addRoomButton: FloatingActionButton = view.btn_addRoomsButton
        addRoomButton.setOnClickListener {
            val action = RoomsFragmentDirections.actionFragmentRoomsToRoomCreateFragment()
            view.findNavController().navigate(action)
        }
    }

}


