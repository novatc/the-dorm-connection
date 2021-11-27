package com.novatc.ap_app.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import com.novatc.ap_app.Firestore.Fireclass
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.findNavController
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import kotlinx.android.synthetic.main.fragment_room_create.view.btn_save_room
import com.novatc.ap_app.model.Room
import kotlinx.android.synthetic.main.fragment_event_create.view.*
import kotlinx.android.synthetic.main.fragment_room_create.*
import java.util.*


class RoomCreateFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_create, container, false)
        initTimePicker(view)
        setSaveRoomButtonListener(view)
        return view
    }

    private fun onCreateRoom(view: View) {
        val roomName = view.created_room_name.text.toString().trim()
        val roomAddress = view.created_room_address.text.toString().trim()
        val roomDescription = view.created_room_description.text.toString().trim()
        var minimumBookingTime = view.created_room_booking_time.text.toString().trim()

        if (roomName.isBlank() || roomAddress.isBlank() || roomDescription.isBlank() || !(minimumBookingTime.matches(Regex("\\d{2}-\\d{2}")))) {
            Toast.makeText(context!!, "All fields are required.", Toast.LENGTH_SHORT).show()
            return
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

    private fun setSaveRoomButtonListener(view: View) {
        val saveRoomButton: Button = view.btn_save_room
        saveRoomButton.setOnClickListener {
            onCreateRoom(view)
            val action = RoomCreateFragmentDirections.actionRoomCreateFragmentToRoomFragment()
            view.findNavController().navigate(action)
        }
    }

    private fun initTimePicker(view: View) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        view.created_room_booking_time.setOnClickListener {
            TimePickerDialog(context!!, this, hour, minute, true).show()
        }

    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        Log.d("Datepicker", "Stunde: $hour Minute: $minute")
        var hourString = hour.toString()
        var minuteString = minute.toString()
        var resultTime = hourString + ":" + minuteString
        created_room_booking_time.text = resultTime
    }
}

