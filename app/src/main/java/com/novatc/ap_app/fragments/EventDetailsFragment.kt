package com.novatc.ap_app.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.novatc.ap_app.R
import com.novatc.ap_app.model.EventWithUser
import kotlinx.android.synthetic.main.event_details_fragment.view.*

class EventDetailsFragment : Fragment() {
    private val args by navArgs<EventDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.event_details_fragment, container, false)
        val event = args.selectedEvent
        view.tv_event_name.text = event.name
        view.tv_event_description.text  = event.text
        view.tv_event_date.text = event.date
        return view
    }


}