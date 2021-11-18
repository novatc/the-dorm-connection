package com.novatc.ap_app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    var layoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: EventViewModel by viewModels()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = LinearLayoutManager(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view =  getView() as View
        val recyclerView: RecyclerView = view.findViewById((R.id.upcoming_events))
        recyclerView.setHasFixedSize(true)
        val adapter = EventsAdapter(eventListItems)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val addEventButton: FloatingActionButton = view.findViewById(R.id.createEventButton)
        addEventButton.setOnClickListener {
            val eventCreateFragment: Fragment = EventCreateFragment()
        }
    }

}