package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertUnixToDate
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertUnixToHoursAndMinutes
import com.novatc.ap_app.fragments.room.RoomDetailsGeneralFragment
import com.novatc.ap_app.model.Booking

class BookingListAdapter( private val listener: RoomDetailsGeneralFragment):
    RecyclerView.Adapter<BookingListAdapter.BookingListViewHolder>() {

    inner class BookingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var startingTime: TextView = itemView.findViewById(R.id.tv_room_starting_time)
        var endTime: TextView = itemView.findViewById(R.id.tv_room_end_time)
        var date: TextView = itemView.findViewById(R.id.tv_date)
        var deleteButton: com.google.android.material.button.MaterialButton = itemView.findViewById(R.id.btn_delete_comment)

        init {
            deleteButton.setOnClickListener(this)
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


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookingListAdapter.BookingListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.booking_list_item, parent, false)
        return BookingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingListAdapter.BookingListViewHolder, position: Int) {
        val booking = differ.currentList[position]
        holder.startingTime.text = convertUnixToHoursAndMinutes(booking.startingDate)
        holder.endTime.text = convertUnixToHoursAndMinutes(booking.endDate)
        holder.date.text = convertUnixToDate(booking.startingDate)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            if(oldItem.startingDate == newItem.startingDate && oldItem.endDate == newItem.endDate){
                return true
            }
            return false
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            if(oldItem.startingDate == newItem.startingDate && oldItem.endDate == newItem.endDate){
                return true
            }
            return false
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount() = differ.currentList.size
}
