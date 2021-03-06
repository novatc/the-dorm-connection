package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertUnixToHoursAndMinutes
import com.novatc.ap_app.model.FreeTimeslot

class FreeTimeslotAdapter:
    RecyclerView.Adapter<FreeTimeslotAdapter.FreeTimeslotViewHolder>() {

    inner class FreeTimeslotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var startingTime: TextView = itemView.findViewById(R.id.free_timeslot_start)
        var endTime: TextView = itemView.findViewById(R.id.free_timeslot_end)
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FreeTimeslotAdapter.FreeTimeslotViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.timeslot_list_item, parent, false)
        return FreeTimeslotViewHolder(view)
    }

    override fun onBindViewHolder(holder: FreeTimeslotAdapter.FreeTimeslotViewHolder, position: Int) {
        val freeTimeslot = differ.currentList[position]
        holder.startingTime.text = convertUnixToHoursAndMinutes(freeTimeslot.startingDate)
        holder.endTime.text = convertUnixToHoursAndMinutes(freeTimeslot.endDate)
    }

    private val differCallback = object : DiffUtil.ItemCallback<FreeTimeslot>() {
        override fun areItemsTheSame(oldItem: FreeTimeslot, newItem: FreeTimeslot): Boolean {
            if(oldItem.startingDate == newItem.startingDate && oldItem.endDate == newItem.endDate){
                return true
            }
            return false
        }

        override fun areContentsTheSame(oldItem: FreeTimeslot, newItem: FreeTimeslot): Boolean {
            if(oldItem.startingDate == newItem.startingDate && oldItem.endDate == newItem.endDate){
                return true
            }
            return false
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount() = differ.currentList.size
}
