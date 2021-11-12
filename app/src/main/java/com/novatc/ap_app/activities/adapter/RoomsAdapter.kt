package com.novatc.ap_app.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import model.RoomsListItem

class RoomsAdapter(private val roomsListItem: ArrayList<RoomsListItem>): RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    class RoomsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val roomTagline: TextView = itemView.findViewById(R.id.roomUsage)
        val roomDescription: TextView = itemView.findViewById(R.id.roomDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.room_list_item, parent, false)
        return RoomsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        val roomsListItem = roomsListItem[position]
        holder.roomName.text = roomsListItem.roomName
        holder.roomTagline.text = roomsListItem.roomTagline
        holder.roomDescription.text = roomsListItem.roomDescription
    }

    override fun getItemCount() = roomsListItem.size

}