package com.novatc.ap_app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.adapter.EventsAdapter
import com.novatc.ap_app.viewModels.EventViewModel
import model.EventListItem


/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {
    private var eventListItems = emptyList<EventListItem>()
    var layoutManager: LinearLayoutManager? = null
    var adapter = EventsAdapter(eventListItems)
    private lateinit var eventsViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EventsAdapter(eventListItems)
        val model: EventViewModel by activityViewModels()
        model.events().observe(this, { _eventListItems ->
            eventListItems += _eventListItems
            adapter.notifyDataSetChanged()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        val recyclerView: RecyclerView = view.findViewById((R.id.upcoming_events))
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        return view
    }


//    val addEventButton: FloatingActionButton = view.findViewById(R.id.createEventButton)
//    addEventButton.setOnClickListener
//    {
//        val eventCreateFragment: Fragment = EventCreateFragment()
//    }

}


