package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Dorm

class DormAdapter(val dormList: ArrayList<Dorm>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<DormAdapter.DormsViewHolder>() {

    inner class DormsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val dormName: TextView = itemView.findViewById(R.id.tv_dorm_name)
        val dormDescription: TextView = itemView.findViewById(R.id.tv_dorm_description)
        val dormAddress: TextView = itemView.findViewById(R.id.tv_dorm_address)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount() = dormList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DormsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dorm_list_item, parent, false)
        return DormsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DormsViewHolder, position: Int) {
        val dormListItem = dormList[position]
        holder.dormName.text = dormListItem.name
        holder.dormDescription.text = dormListItem.description
        holder.dormAddress.text = dormListItem.address
    }
}