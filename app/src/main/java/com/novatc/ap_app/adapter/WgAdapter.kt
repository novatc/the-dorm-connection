package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Wg
import kotlinx.android.synthetic.main.wg_list_item.view.*

class WgAdapter(
    private val listener: OnWgClickListener
): RecyclerView.Adapter<WgAdapter.WgViewHolder>() {

    inner class WgViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        val wgName: TextView = itemView.wgItemName
        val wgSlogan: TextView = itemView.wgItemSlogan

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Wg>() {
        override fun areItemsTheSame(oldItem: Wg, newItem: Wg): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Wg, newItem: Wg): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }
                oldItem.name != newItem.name -> {
                    false
                }
                oldItem.slogan != newItem.slogan -> {
                    false
                }
                else -> true
            }
        }
    }


    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WgViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wg_list_item, parent, false)
        return WgViewHolder(view)
    }

    override fun onBindViewHolder(holder: WgViewHolder, position: Int) {
        val wgListItem = differ.currentList[position]
        holder.wgName.text = wgListItem.name
        holder.wgSlogan.text = wgListItem.slogan
    }

    override fun getItemCount() = differ.currentList.size

    interface OnWgClickListener {
        fun onItemClick(position: Int)
    }
}

