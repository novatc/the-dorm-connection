package com.novatc.ap_app.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Event


class EventsAdapter(
    private val listener: OnItemClickListener,
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
    private val differCallback = object: DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }
                oldItem.name != newItem.name -> {
                    false
                }
                oldItem.text != newItem.text -> {
                    false
                }
                oldItem.authorId != newItem.authorId -> {
                    false
                }
                oldItem.date != newItem.date -> {
                    false
                }
                oldItem.city != newItem.city -> {
                    false
                }
                else -> true
            }
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false)
        return EventsViewHolder(view, onLocationClick)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val eventListItem = differ.currentList[position]
        holder.eventAuthor.text = eventListItem.authorName
        holder.eventName.text = eventListItem.name
        holder.eventText.text = eventListItem.text
        holder.eventDate.text = eventListItem.date
    }

    override fun getItemCount() = differ.currentList.size

}


