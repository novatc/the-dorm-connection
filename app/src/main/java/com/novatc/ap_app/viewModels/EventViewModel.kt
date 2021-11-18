package com.novatc.ap_app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novatc.ap_app.fragments.*
import model.EventListItem

class EventViewModel: ViewModel() {
    private val events: MutableLiveData<List<EventListItem>> by lazy {
        MutableLiveData<List<EventListItem>>().also {
            loadEvents()
        }
    }

    fun getEvents(): LiveData<List<EventListItem>> {
        return events
    }

    private fun loadEvents() {
        val exampleEventAuthor = "Pinte 42"
        val exampleEventName = "Pintenmittwoch "
        val exampleEventText = "Join us every Wednesday with your study budies to grab a couple of beers for cheap :)"
        val exampleEventDate = "Heute 20:00"

        val eventListItems: ArrayList<EventListItem> =  ArrayList()
            for (i in 0..5) {
                eventListItems.add(EventListItem(
                    exampleEventAuthor,
                    exampleEventName + i,
                    exampleEventText,
                    exampleEventDate
                ))
            }

    }
}