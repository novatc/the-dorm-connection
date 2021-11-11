package com.novatc.ap_app.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import model.EventListItem

class EventsAdapter(private val eventListItems: ArrayList<EventListItem>): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

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
        holder.eventAuthor.text = eventListItem.eventAuthor
        holder.eventName.text = eventListItem.eventName
        holder.eventText.text = eventListItem.eventText
        holder.eventDate.text = eventListItem.eventDate
    }

    override fun getItemCount() = eventListItems.size

}