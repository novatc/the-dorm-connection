package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Room

class RoomsAdapter(
    private val roomsListItem: List<Room>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    inner class RoomsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val roomName: TextView = itemView.findViewById(R.id.roomName)
        val roomAddress: TextView = itemView.findViewById(R.id.roomUsage)
        val roomDescription: TextView = itemView.findViewById(R.id.roomDescription)
        val minimumBookingTime: TextView = itemView.findViewById(R.id.roomBookingTimeField)

        init {
            itemView.setOnClickListener(this)
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
        return RoomsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        val roomsListItem = roomsListItem[position]
        holder.roomName.text = roomsListItem.name
        holder.roomAddress.text = roomsListItem.address
        holder.roomDescription.text = roomsListItem.text
        holder.minimumBookingTime.text = roomsListItem.minimumBookingTime
    }

    override fun getItemCount() = roomsListItem.size

}