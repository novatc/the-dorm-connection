package com.novatc.ap_app.activities.fragments

import Firestore.Fireclass
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_event_create.view.*
import kotlinx.android.synthetic.main.fragment_event_create.view.btn_safe_new_event
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import model.Event
import model.Room


class EventCreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_event_create, container, false)
        view.btn_safe_new_event.setOnClickListener {
            val eventList = EventFragment()
            val event = Event(
                view.et_name_of_event.text.toString(),
                view.et_date_of_event.text.toString(),
                Fireclass().getCurrentUserID()
            )
            Fireclass().addEventToDD(event)
            Toast.makeText(requireActivity(), "Event created", Toast.LENGTH_SHORT).show()
            parentFragmentManager.commit {
                replace(R.id.fragment_container, eventList)
            }
        }

        return view
    }
}