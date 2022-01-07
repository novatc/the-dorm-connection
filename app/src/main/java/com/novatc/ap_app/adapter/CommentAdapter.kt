package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Comment

class CommentAdapter(val commentList: ArrayList<Comment>):
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var author: TextView = itemView.findViewById(R.id.tv_comment_author)
        var commentContent: TextView = itemView.findViewById(R.id.tv_comment_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_comments, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.author.text = comment.author
        holder.commentContent.text = comment.content
    }

    override fun getItemCount() = commentList.size
    }
