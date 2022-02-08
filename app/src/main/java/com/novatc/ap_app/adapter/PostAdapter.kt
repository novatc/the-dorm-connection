package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Event
import com.novatc.ap_app.model.Post

class PostAdapter(
    private val listener: OnItemClickListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val postHeadline: TextView = itemView.findViewById(R.id.tv_post_headline)
        val postText: TextView = itemView.findViewById(R.id.tv_post_text)
        val postKeywords: TextView = itemView.findViewById(R.id.tv_post_keyword)
        val postAuthor: TextView = itemView.findViewById(R.id.tv_post_author)
        val date: TextView = itemView.findViewById(R.id.tv_post_date)
        var key: String = ""

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.post_list_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postListItem = differ.currentList[position]
        holder.postHeadline.text = postListItem.headline
        holder.postAuthor.text = postListItem.creator
        holder.postText.text = postListItem.text
        holder.postKeywords.text = postListItem.keyword
        holder.date.text = postListItem.date?.let { convertTime(it) }
        holder.key = postListItem.id.toString()
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }
                oldItem.creatorID != newItem.creatorID -> {
                    false
                }
                oldItem.text != newItem.text -> {
                    false
                }
                oldItem.creator != newItem.creator -> {
                    false
                }

                oldItem.headline != newItem.headline -> {
                    false
                }
                oldItem.keyword != newItem.keyword -> {
                    false
                }
                oldItem.creator != newItem.creator -> {
                    false
                }
                else -> true
            }
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    private fun convertTime(unixTimestamp:Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm")
        val date = java.util.Date(unixTimestamp)
        return sdf.format(date)
    }
}