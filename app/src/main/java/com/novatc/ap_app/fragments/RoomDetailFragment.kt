package com.novatc.ap_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.room_detail_fragment.view.*

class RoomDetailFragment : Fragment() {
    private val args by navArgs<RoomDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.room_detail_fragment, container, false)
        val room = args.selectedRoom

        view.eventDetailsName.text = room.text
        view.tv_room_description.text = room.text
        return view
    }


}