package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.RoomsAdapter
import com.novatc.ap_app.fragments.PinboardFragment
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.services.SwipeGestureListener
import com.novatc.ap_app.services.SwipeListener
import com.novatc.ap_app.viewModels.room.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_room_list.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class RoomsFragment : Fragment(), RoomsAdapter.OnItemClickListener, SwipeListener {
    var roomList:List<Room> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)
        fillRoomsList(view)
        setAddRoomButtonListener(view)
        view.setOnTouchListener(SwipeGestureListener(this))
        view.available_rooms.setOnTouchListener(SwipeGestureListener(this))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var back1 = view.findNavController().backQueue
        print("test")
    }

    override fun onItemClick(position: Int) {
        val room = roomList[position]
        val action = RoomsFragmentDirections.actionFragmentRoomsToRoomDetailsFragment(room)
        findNavController().navigate(action)
    }

    @ExperimentalCoroutinesApi
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

    override fun onSwipeLeft(view: View) {
        val action = RoomsFragmentDirections.actionFragmentRoomsToFragmentEvents()
        view.findNavController().navigate(action)
    }

    override fun onSwipeRight(view: View) {
        var back1 = view.findNavController().backQueue
        val action = RoomsFragmentDirections.actionFragmentRoomsToFragmentPinboard()
        view.findNavController().navigate(action)
        var back2 = view.findNavController().backQueue
        //view.findNavController().backQueue.addLast()
        print("test")
    }

}


