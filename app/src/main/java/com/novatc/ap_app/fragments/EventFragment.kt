package com.novatc.ap_app.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.adapter.EventsAdapter
import com.novatc.ap_app.viewModels.EventViewModel

class EventFragment : Fragment() {
    private lateinit var layoutManager: LinearLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        fillEventsList(view)
        val addEventButton: FloatingActionButton = view.findViewById(R.id.createEventButton)
        addEventButton.setOnClickListener {
            val eventCreateFragment: Fragment = EventCreateFragment()
            parentFragmentManager.commit {
                isAddToBackStackAllowed
                setReorderingAllowed(true)
                replace(R.id.fragment_container, eventCreateFragment)
            }
        }
        return view
    }

    private fun fillEventsList(view: View) {
        val recyclerView: RecyclerView = view.findViewById((R.id.upcoming_events))
        val model = ViewModelProvider(this)[EventViewModel::class.java]
        model.events.observe(this, { events ->
            recyclerView.adapter = EventsAdapter(events)
        })
        recyclerView.layoutManager = layoutManager
    }

}


