package com.novatc.ap_app.fragments.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.EventDetailsStateAdapter
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.viewModels.event.EventDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event_details.view.*

@AndroidEntryPoint
class EventDetailsFragment : Fragment() {

    private lateinit var eventDetailsAdapter: EventDetailsStateAdapter
    private lateinit var viewPager: ViewPager2
    private val tabNames = listOf(
        R.string.detail_event_general,
        R.string.detail_event_location,
        R.string.detail_event_join
    )
    private val args by navArgs<EventDetailsFragmentArgs>()
    val model: EventDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_details, container, false)
        val event = args.event
        model.setEvent(event)
        view.eventDetailsName.text = event.name
        view.eventDetailsDate.text = event.date
        loadImage(view)
        return view
    }

    // Load image for event detail
    private fun loadImage(view: View) {
        model.loadEventImage()
        model.loadImageRequest.observe(this, { request ->
            if (request.status == Request.Status.SUCCESS) {
                val imageUri = request.data
                Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .placeholder(R.drawable.circular_progress_bar)
                    .into(view.eventDetailsImage)
            }
        })
    }

    // Setup tabs
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventDetailsAdapter = EventDetailsStateAdapter(this)
        viewPager = view.eventDetailsPager
        viewPager.adapter = eventDetailsAdapter
        val tabLayout = view.eventDetailsTabs
        TabLayoutMediator(
            tabLayout,
            viewPager
        ) { tab, position -> tab.setText(tabNames[position]) }.attach()
    }


}