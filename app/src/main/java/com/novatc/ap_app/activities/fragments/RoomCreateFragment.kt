package com.novatc.ap_app.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_room_create.view.*


class RoomCreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_room_create, container, false)
        view.btn_safe_new_event.setOnClickListener {
            Toast.makeText(requireActivity(), "Room created", Toast.LENGTH_SHORT).show()
            val roomList = RoomListFragment()
            parentFragmentManager.commit {
                replace(R.id.fragment_container, roomList)
            }
        }

        return view
    }
}

