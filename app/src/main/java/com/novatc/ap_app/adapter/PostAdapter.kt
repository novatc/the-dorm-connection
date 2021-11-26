package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Post
import kotlinx.coroutines.flow.Flow

class PostAdapter(val postListItems: ArrayList<Post>):
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postHeadline: TextView = itemView.findViewById(R.id.tv_post_headline)
        val postText: TextView = itemView.findViewById(R.id.tv_post_text)
        val postKeywords: TextView = itemView.findViewById(R.id.tv_post_keyword)
        val postAuthor: TextView = itemView.findViewById(R.id.tv_post_author)
        val date: TextView = itemView.findViewById(R.id.tv_post_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.post_list_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postListItem = postListItems[position]
        holder.postHeadline.text = postListItem.headline
        holder.postAuthor.text = postListItem.creator
        holder.postText.text = postListItem.text
        holder.postKeywords.text = postListItem.keyword
        holder.date.text = postListItem.date
    }

    override fun getItemCount() = postListItems.size
}