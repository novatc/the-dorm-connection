package com.novatc.ap_app.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.EventWithUser

class EventsAdapter(private val eventListItems: ArrayList<EventWithUser>): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    class EventsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val eventAuthor: TextView = itemView.findViewById(R.id.eventAuthor)
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
        if (eventListItem.user != null) {
            holder.eventAuthor.text = eventListItem.user!!.username
        } else {
            holder.eventAuthor.text = "NO USER"
        }

        holder.eventName.text = eventListItem.name
        holder.eventText.text = eventListItem.text
        holder.eventDate.text = eventListItem.date
    }

    override fun getItemCount() = eventListItems.size

}