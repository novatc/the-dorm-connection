package com.novatc.ap_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.adapter.EventsAdapter
import com.novatc.ap_app.viewModels.EventViewModel
import kotlinx.android.synthetic.main.fragment_event.view.*

/**
 * The event fragment displays a list of events
 */
class EventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        fillEventsList(view)
        setAddEventButtonListener(view)
        return view
    }

    /**
     * Sets an on click listener to navigate to the EventCreate fragment
     * @param view
     */
    private fun setAddEventButtonListener(view: View) {
        val addEventButton: FloatingActionButton = view.createEventButton
        addEventButton.setOnClickListener {
            val action = EventFragmentDirections.actionEventFragmentToEventCreateFragment()
            view.findNavController().navigate(action)
        }
    }

    /**
     * Observes events and sets the recyclerViews adapter and layoutManager
     * @param view
     */
    private fun fillEventsList(view: View) {
        val recyclerView: RecyclerView = view.upcoming_events
        val model: EventViewModel by viewModels()
        model.events.observe(this, { events ->
            recyclerView.adapter = EventsAdapter(events)
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

}


