package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.room.RoomDateHelper
import com.novatc.ap_app.model.Room

class RoomsAdapter(
    private val listener: OnItemClickListener,
    private val onLocationClick: (Int) -> Unit
) : RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    inner class RoomsViewHolder(itemView: View, val onLocationClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val roomDescription: TextView = itemView.findViewById(R.id.roomShortDescription)
        val minimumBookingTime: TextView = itemView.findViewById(R.id.roomMinBookingTime)
        val maximumBookingTime: TextView = itemView.findViewById(R.id.roomMaxBookingTime)
        val roomLocationButton: FloatingActionButton =
            itemView.findViewById(R.id.roomLocation)
        init {
            itemView.setOnClickListener(this)
            roomLocationButton.setOnClickListener {
                onLocationClick(layoutPosition)
            }
        }

        override fun onClick(v: View?) {
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }

        }
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.room_list_item, parent, false)
        return RoomsViewHolder(view, onLocationClick)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }
                oldItem.name != newItem.name -> {
                    false
                }
                oldItem.description != newItem.description -> {
                    false
                }
                oldItem.streetName != newItem.streetName -> {
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

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        val roomsListItem = differ.currentList[position]
        holder.roomName.text = roomsListItem.name
        holder.roomDescription.text = roomsListItem.shortDescription
        holder.minimumBookingTime.text = roomsListItem.minimumBookingTime?.let {
            RoomDateHelper.convertMillisToHoursAndMinutes(
                it.toLong())
        }
        holder.maximumBookingTime.text = roomsListItem.maximumBookingTime?.let {
            RoomDateHelper.convertMillisToHoursAndMinutes(
                it.toLong())
        }
    }

    override fun getItemCount() = differ.currentList.size

}