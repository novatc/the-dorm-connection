package com.novatc.ap_app.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import model.BaseActivity
import model.EventListItem

class EventsOverviewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_overview)

        val eventListItems: ArrayList<EventListItem> =  ArrayList()
        val exampleEventAuthor = "Pinte 42"
        val exampleEventName = "Pintenmittwoch "
        val exampleEventText = "Join us every Wednesday with your study budies to grab a couple of beers for cheap :)"
        val exampleEventDate = "Heute 20:00"

        for (i in 0..5) {
            eventListItems.add(EventListItem(
                exampleEventAuthor,
                exampleEventName + i,
                exampleEventText,
                exampleEventDate
            ))
        }

        val recyclerView: RecyclerView = findViewById((R.id.upcoming_events))
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        val adapter = EventsAdapter(eventListItems)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }
}