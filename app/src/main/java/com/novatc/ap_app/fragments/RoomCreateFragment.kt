package com.novatc.ap_app.fragments

import com.novatc.ap_app.Firestore.Fireclass
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import kotlinx.android.synthetic.main.fragment_room_create.view.btn_save_room
import com.novatc.ap_app.model.Room



class RoomCreateFragment : Fragment() {
    private var roomBookingTime = "01:00"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_create, container, false)
        view.btn_save_room.setOnClickListener {
            setSaveRoomButtonListener(view)
            onCreateRoom(view)
        }
        return view
    }
    private fun onCreateRoom(view: View) {
        val roomName = view.created_room_name.text.toString().trim()
        val roomAddress = view.created_room_address.text.toString().trim()
        val roomDescription = view.created_room_description.text.toString().trim()
        var minimumBookingTime = view.created_room_booking_time.text.toString().trim()

        if (roomName.isBlank() || roomDescription.isBlank() || roomBookingTime.isBlank()) {
            Toast.makeText(requireContext(), "All fields are required.", Toast.LENGTH_SHORT).show()
            return
        }
        if (minimumBookingTime.isBlank()){
            minimumBookingTime = "01:00"
        }

        val room = Room(
            roomName,
            roomAddress,
            Fireclass().getCurrentUserID(),
            roomDescription,
            minimumBookingTime
        )
        Fireclass().addRoomToDD(room)
        Toast.makeText(requireContext(), "Room created", Toast.LENGTH_SHORT).show()

    }

    private fun minimumBookingTime(bookingTime: String){

    }
    private fun setSaveRoomButtonListener(view: View) {
        val saveRoomButton: Button = view.btn_save_room
        saveRoomButton.setOnClickListener {
            val action = RoomCreateFragmentDirections.actionRoomCreateFragmentToRoomFragment()
            view.findNavController().navigate(action)
        }
    }
}

