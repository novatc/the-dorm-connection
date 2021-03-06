package com.novatc.ap_app.fragments.event

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.EventsAdapter
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.services.SwipeGestureListener
import com.novatc.ap_app.services.SwipeListener
import com.novatc.ap_app.viewModels.event.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * The event fragment displays a list of events
 */
@AndroidEntryPoint
class EventFragment : Fragment(), EventsAdapter.OnItemClickListener, SwipeListener {

    var eventList:List<Event> = emptyList()
    val model: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        fillEventsList(view)
        setAddEventButtonListener(view)
        setMapOverviewButtonListener(view)
        view.events_constraintLayout.setOnTouchListener(SwipeGestureListener(this))
        view.setOnTouchListener(SwipeGestureListener(this))
        return view
    }

    // Navigate on plus button click to create event view
    private fun setAddEventButtonListener(view: View) {
        val addEventButton: FloatingActionButton = view.createEventButton
        addEventButton.setOnClickListener {
            view.findNavController().navigate(EventFragmentDirections.actionEventFragmentToEventCreateFragment())
        }
    }

    // Navigate to the event overview map
    private fun setMapOverviewButtonListener(view: View){
        val mapOverviewButton = view.btn_viewEventMap
        mapOverviewButton.setOnClickListener {
            view.findNavController().navigate(EventFragmentDirections.actionFragmentEventsToEventOverviewMapsFragment())
        }
    }

    // Navigate on event click to event detail view
    override fun onItemClick(position: Int) {
        val event = eventList[position]
        val action = EventFragmentDirections.actionFragmentEventsToEventDetailsFragment(event)
        findNavController().navigate(action)
    }

    // Opens Google Maps on location button click
    private fun onLocationClick(position: Int) {
        val event = eventList[position]
        val mapSearch = "${event.streetName} ${event.houseNumber}, ${event.city}"
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(mapSearch))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
            Snackbar.make(bottomNavView,  R.string.no_maps, Snackbar.LENGTH_SHORT).apply {
                anchorView = bottomNavView
            }.show()
        }

    }

    // Setup recyclerview with events
    @ExperimentalCoroutinesApi
    private fun fillEventsList(view: View) {
        val recyclerView: RecyclerView = view.upcoming_events
        view.eventsListSpinner.visibility = View.VISIBLE
        val eventsAdapter = EventsAdapter(this) { position -> onLocationClick(position) }
        recyclerView.adapter = eventsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        model.events.observe(this, { events ->
            view.eventsListSpinner.visibility = View.GONE
            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val result = events.sortedByDescending {
                LocalDate.parse(it.date, dateTimeFormatter)
            }
            eventsAdapter.differ.submitList(result)
            eventList = result

        })
    }

    override fun onSwipeLeft(view: View) {
        val action = EventFragmentDirections.actionFragmentEventsToFragmentProfile()
        findNavController().navigate(action)
    }

    override fun onSwipeRight(view: View) {
        val action = EventFragmentDirections.actionFragmentEventsToFragmentRooms()
        findNavController().navigate(action)
    }

}


