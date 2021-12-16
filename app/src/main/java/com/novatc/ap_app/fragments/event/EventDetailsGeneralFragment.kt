package com.novatc.ap_app.fragments.event

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.R
import com.novatc.ap_app.viewModels.event.EventDetailsViewModel
import kotlinx.android.synthetic.main.fragment_event_details_general.view.*


class EventDetailsGeneralFragment : Fragment() {
    val model: EventDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_details_general, container, false)
        model.event.observe(this, { event ->
            view.eventDetailsDescription.text = event.text
        })

        return view
    }
}