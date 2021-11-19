package com.novatc.ap_app.fragments

import Firestore.Fireclass
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.RoomListFragment
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import model.Room


class RoomCreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_create, container, false)
        view.btn_safe_new_event.setOnClickListener {
            val room = Room(
                view.et_room_name.text.toString(),
                view.et_room_address.text.toString(),
                Fireclass().getCurrentUserID()
            )
            Fireclass().addRoomToDD(room)
            Toast.makeText(requireActivity(), "Room created", Toast.LENGTH_SHORT).show()
            val roomList = RoomListFragment()
            parentFragmentManager.commit {
                replace(R.id.fragment_container, roomList)
            }
        }

        return view
    }
}

