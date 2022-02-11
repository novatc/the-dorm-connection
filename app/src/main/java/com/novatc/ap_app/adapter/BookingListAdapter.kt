package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.room.RoomDateHelper
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertUnixToDate
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertUnixToHoursAndMinutes
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.FreeTimeslot

class BookingListAdapter:
    RecyclerView.Adapter<BookingListAdapter.BookingListViewHolder>() {

    inner class BookingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var startingTime: TextView = itemView.findViewById(R.id.free_timeslot_start)
        var endTime: TextView = itemView.findViewById(R.id.free_timeslot_end)
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingListAdapter.BookingListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.booking_list_item, parent, false)
        return BookingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingListAdapter.BookingListViewHolder, position: Int) {
        val freeTimeslot = differ.currentList[position]
        holder.startingTime.text = convertUnixToHoursAndMinutes(freeTimeslot.startingDate)
        holder.endTime.text = convertUnixToHoursAndMinutes(freeTimeslot.endDate)
    }

    private val differCallback = object : DiffUtil.ItemCallback<FreeTimeslot>() {
        override fun areItemsTheSame(oldItem: FreeTimeslot, newItem: FreeTimeslot): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: FreeTimeslot, newItem: FreeTimeslot): Boolean {
            TODO("Not yet implemented")
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount() = differ.currentList.size
}
