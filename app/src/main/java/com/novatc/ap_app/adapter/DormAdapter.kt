package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Dorm
import com.novatc.ap_app.model.Post

class DormAdapter(private val listener: OnItemClickListener) :
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

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DormsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.dorm_list_item, parent, false)
        return DormsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DormsViewHolder, position: Int) {
        val dormListItem = differ.currentList[position]
        holder.dormName.text = dormListItem.name
        holder.dormDescription.text = dormListItem.description
        holder.dormAddress.text = dormListItem.address
    }

    private val differCallback = object : DiffUtil.ItemCallback<Dorm>() {
        override fun areItemsTheSame(oldItem: Dorm, newItem: Dorm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dorm, newItem: Dorm): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }
                oldItem.address != newItem.address -> {
                    false
                }
                oldItem.description != newItem.description -> {
                    false
                }
                oldItem.id != newItem.id -> {
                    false
                }

                oldItem.name != newItem.name -> {
                    false
                }
                oldItem.roomList != newItem.roomList -> {
                    false
                }
                oldItem.userList != newItem.userList -> {
                    false
                }
                else -> true
            }
        }

    }

    val differ = AsyncListDiffer(this, differCallback)
}