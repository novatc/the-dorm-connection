package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.R
import com.novatc.ap_app.viewModels.room.RoomDetailsViewModel
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