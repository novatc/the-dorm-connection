package com.novatc.ap_app.fragments.room

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.adapter.EventsAdapter
import com.novatc.ap_app.adapter.RoomsAdapter
import com.novatc.ap_app.fragments.PinboardFragment
import com.novatc.ap_app.model.Room
import com.novatc.ap_app.services.SwipeGestureListener
import com.novatc.ap_app.services.SwipeListener
import com.novatc.ap_app.viewModels.room.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.view.*
import kotlinx.android.synthetic.main.fragment_room_list.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        view.rooms_constraintLayout.setOnTouchListener(SwipeGestureListener(this))
        return view
    }

    override fun onItemClick(position: Int) {
        val room = roomList[position]
        val action = RoomsFragmentDirections.actionFragmentRoomsToRoomDetailsFragment(room)
        findNavController().navigate(action)
    }

    @ExperimentalCoroutinesApi
    private fun fillRoomsList(view: View) {
        view.roomsListSpinner.visibility = View.VISIBLE
        val recyclerView: RecyclerView = view.available_rooms
        val roomsAdapter = RoomsAdapter(this) { position -> onLocationClick(position) }
        recyclerView.adapter = roomsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val model: RoomViewModel by viewModels()
        model.rooms.observe(this, { rooms ->
            roomsAdapter.differ.submitList(rooms)
            roomList = rooms
            view.roomsListSpinner.visibility = View.GONE
        })
    }

    // Opens Google Maps on location button click
    private fun onLocationClick(position: Int) {
        val room = roomList[position]
        val mapSearch = "${room.streetName} ${room.houseNumber}, ${room.city}"
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(mapSearch))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView,  R.string.no_maps, Snackbar.LENGTH_SHORT).apply {
                anchorView = bottomNavView
            }.show()
        }

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


