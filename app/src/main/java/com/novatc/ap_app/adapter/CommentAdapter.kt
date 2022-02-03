package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.model.Post

class CommentAdapter(val commentList: ArrayList<Comment>, userID: String, private val listener: OnItemClickListener) :

    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    var userID = userID

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var author: TextView = itemView.findViewById(R.id.tv_comment_author)
        var commentContent: TextView = itemView.findViewById(R.id.tv_comment_content)
        var authorID: String = ""
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
    ): CommentAdapter.CommentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_comments, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.author.text = comment.authorName
        holder.commentContent.text = comment.content
        holder.authorID = comment.authorID

        if (userID == holder.authorID) {
            holder.deleteButton.visibility = View.VISIBLE
        } else {
            holder.deleteButton.visibility = View.GONE
        }




    }

    private val differCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }
                oldItem.authorID != newItem.authorID -> {
                    false
                }
                oldItem.authorName != newItem.authorName -> {
                    false
                }
                oldItem.content != newItem.content -> {
                    false
                }
                else -> true
            }
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount() = commentList.size
}
