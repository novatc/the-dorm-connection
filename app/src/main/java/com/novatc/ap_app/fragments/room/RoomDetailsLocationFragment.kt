package com.novatc.ap_app.fragments.room

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.R
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import kotlinx.android.synthetic.main.fragment_event_details_location.view.*
import kotlinx.android.synthetic.main.fragment_room_details_location.view.*

class RoomDetailsLocationFragment: Fragment() {
    val model: RoomDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_details_location, container, false)
        model.room.observe(this, { room ->
            //view.eventDetailsAddress.text = "${room.address}"
            //setOnLocationListener(view, room.address!!, room.address!!, room.address!!)
        })
        return view
    }

    private fun setOnLocationListener(
        view: View,
        streetName: String,
        houseNumber: String,
        city: String
    ) {
        view.roomDetailsMapsButton.setOnClickListener {
            val mapSearch = "$streetName ${houseNumber}, $city"
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(mapSearch))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, R.string.no_maps, Toast.LENGTH_SHORT).show()
            }
        }
    }
}