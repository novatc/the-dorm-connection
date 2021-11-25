package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import model.RoomWithUser

class RoomsAdapter(private val roomsListItem: ArrayList<RoomWithUser>): RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    class RoomsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val roomAddress: TextView = itemView.findViewById(R.id.roomUsage)
        val roomDescription: TextView = itemView.findViewById(R.id.roomDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.room_list_item, parent, false)
        return RoomsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        val roomsListItem = roomsListItem[position]
        holder.roomName.text = roomsListItem.name
        holder.roomAddress.text = roomsListItem.address
        holder.roomDescription.text = roomsListItem.text
    }

    override fun getItemCount() = roomsListItem.size

}