package com.novatc.ap_app.fragments.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.viewModels.event.EventDetailsViewModel
import kotlinx.android.synthetic.main.fragment_event_details_general.view.*


class EventDetailsGeneralFragment : Fragment() {
    val model: EventDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_details_general, container, false)
        // Observe event and setup event description
        model.event.observe(this, { event ->
            view.eventDetailsDescription.text = event.text
        })
        // Show delete button if current user is event author
        if (model.isEventAuthor()) {
            view.eventDeleteButton.visibility = View.VISIBLE
        }
        // Setup on event delete click listener
        view.eventDeleteButton.setOnClickListener {
            model.deleteEvent()
            model.deleteEventRequest.observe(this, { request ->
                when (request.status) {
                    Request.Status.SUCCESS -> {
                        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                        Snackbar.make(bottomNavView, R.string.details_event_delete_successfull, Snackbar.LENGTH_SHORT).apply {
                            anchorView = bottomNavView
                        }.show()
                        view.findNavController().navigate(EventDetailsFragmentDirections.actionEventDetailsFragmentToFragmentEvents())
                    }

                    else -> {
                        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                        Snackbar.make(bottomNavView,  request.message!!, Snackbar.LENGTH_LONG).apply {
                            anchorView = bottomNavView
                        }.show()
                    }
                }
            })
        }


        return view
    }
}