package com.novatc.ap_app.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import model.Event

class EventsAdapter(private val eventListItems: ArrayList<Event>): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    class EventsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val eventAuthor: TextView = itemView.findViewById(R.id.eventName)
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventText: TextView = itemView.findViewById(R.id.eventText)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false)
        return EventsViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val eventListItem = eventListItems[position]
        holder.eventAuthor.text = eventListItem.author
        holder.eventName.text = eventListItem.name
        holder.eventText.text = eventListItem.text
        holder.eventDate.text = eventListItem.date
    }

    override fun getItemCount() = eventListItems.size

}