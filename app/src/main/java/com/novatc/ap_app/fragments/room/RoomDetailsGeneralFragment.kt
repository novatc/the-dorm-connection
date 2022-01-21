package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.event.EventDetailsFragmentDirections
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import com.novatc.ap_app.viewModels.event.EventDetailsViewModel
import kotlinx.android.synthetic.main.fragment_event_details_general.view.*
import kotlinx.android.synthetic.main.fragment_room_details_general.view.*


class RoomDetailsGeneralFragment : Fragment() {
    val model: RoomDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_details_general, container, false)
        model.room.observe(this, { room ->
            view.room_details_description.text = room.description
        })
        return view
    }
}