package com.novatc.ap_app.fragments.event

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.viewModels.event.EventDetailsViewModel
import kotlinx.android.synthetic.main.fragment_event_details_location.view.*


class EventDetailsLocationFragment : Fragment() {
    val model: EventDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_details_location, container, false)
        model.event.observe(this, { event ->
            view.eventDetailsAddress.text = "${event.streetName} ${event.houseNumber}"
            view.eventDetailsCity.text = event.city
            setOnLocationListener(view, event.streetName!!, event.houseNumber!!, event.city!!)
        })
        return view
    }

    private fun setOnLocationListener(view: View, streetName: String, houseNumber: String, city: String ) {
        view.eventDetailsMapsButton.setOnClickListener {
            val mapSearch = "$streetName ${houseNumber}, $city"
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(mapSearch))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView, R.string.no_maps, Snackbar.LENGTH_LONG).apply {
                    anchorView = bottomNavView
                }.show()
            }
        }
    }
}