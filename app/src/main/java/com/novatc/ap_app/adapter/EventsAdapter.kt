package com.novatc.ap_app.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.model.EventWithUser


class EventsAdapter(
    private val eventListItems: ArrayList<EventWithUser>,
    private val listener: OnItemClickListener
    private val listener: EventsAdapter.OnItemClickListener,
    private val onLocationClick: (Int) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    inner class EventsViewHolder(itemView: View, val onLocationClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val eventAuthor: TextView = itemView.findViewById(R.id.tv_dorm_name)
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventText: TextView = itemView.findViewById(R.id.tv_dorm_description)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        val eventLocationButton: FloatingActionButton = itemView.findViewById(R.id.button_eventLocation)

        init {
            itemView.setOnClickListener(this)
            eventLocationButton.setOnClickListener {
                onLocationClick(layoutPosition)
            }
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v?.id == eventLocationButton.id) {
                    Toast.makeText(v.context, "Location pressed", Toast.LENGTH_SHORT).show()
                } else {
                    listener.onItemClick(position)
                }
            }

        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false)
        return EventsViewHolder(view, onLocationClick)
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