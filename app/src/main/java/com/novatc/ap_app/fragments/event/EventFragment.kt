package com.novatc.ap_app.fragments.event

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.adapter.EventsAdapter
import com.novatc.ap_app.model.EventWithUser
import com.novatc.ap_app.viewModels.event.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.view.*

/**
 * The event fragment displays a list of events
 */
@AndroidEntryPoint
class EventFragment : Fragment(), EventsAdapter.OnItemClickListener {

    var eventList:ArrayList<EventWithUser> = ArrayList()
    lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        fillEventsList(view)
        setAddEventButtonListener(view)
        return view
    }


    private fun setAddEventButtonListener(view: View) {
        val addEventButton: FloatingActionButton = view.createEventButton
        addEventButton.setOnClickListener {
            view.findNavController().navigate(EventFragmentDirections.actionEventFragmentToEventCreateFragment())
        }
    }

    override fun onItemClick(position: Int) {
        val event = eventList[position]
        val action = EventFragmentDirections.actionFragmentEventsToEventDetailsFragment(event)
        findNavController().navigate(action)
    }

    private fun onLocationClick(position: Int) {
        val event = eventList[position]
        val mapSearch = "${event.streetName} ${event.houseNumber}, ${event.city}"
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(mapSearch))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, R.string.no_maps, Toast.LENGTH_SHORT).show()
        }

    }


    private fun fillEventsList(view: View) {
        val recyclerView: RecyclerView = view.upcoming_events
        val model: EventViewModel by viewModels()
        view.eventsListSpinner.visibility = View.VISIBLE
        eventsAdapter = EventsAdapter( this) { position -> onLocationClick(position) }
        recyclerView.adapter = eventsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        model.events.observe(this, { events ->
            view.eventsListSpinner.visibility = View.GONE
            eventsAdapter.differ.submitList(events)
            eventList = events

        })
    }

}


