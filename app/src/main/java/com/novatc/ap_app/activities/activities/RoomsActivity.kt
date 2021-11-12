package com.novatc.ap_app.activities.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.activities.adapter.EventsAdapter
import com.novatc.ap_app.activities.adapter.RoomsAdapter
import model.BaseActivity
import model.EventListItem
import model.RoomsListItem

class RoomsActivity : BaseActivity() {
    val roomsListItems: ArrayList<RoomsListItem> =  ArrayList()
    val exampleRoomName = "Pinte 42"
    val exampleRoomTagline = "Bar and party room"
    val exampleRoomDescription = "Have parties or social gatherings in the Pinte 42!"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        for (i in 0..5) {
            roomsListItems.add(
                RoomsListItem(
                    exampleRoomName,
                    exampleRoomTagline + i,
                    exampleRoomDescription
            )
            )
        }

        val recyclerView: RecyclerView = findViewById((R.id.available_rooms))
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        val adapter = RoomsAdapter(roomsListItems)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        for (i in 0..5) {
            roomsListItems.add(
                RoomsListItem(
                    exampleRoomName,
                    exampleRoomTagline + i,
                    exampleRoomDescription
            )
            )
        }

        val recyclerView: RecyclerView = findViewById((R.id.available_rooms))
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        val adapter = RoomsAdapter(roomsListItems)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}