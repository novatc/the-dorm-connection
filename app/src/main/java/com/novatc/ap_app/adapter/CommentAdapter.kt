package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Comment
import com.novatc.ap_app.repository.PostRepository
import kotlinx.android.synthetic.main.fragment_comments.view.*
import kotlinx.android.synthetic.main.fragment_post_details.view.*

class CommentAdapter(val commentList: ArrayList<Comment>) :

    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var author: TextView = itemView.findViewById(R.id.tv_comment_author)
        var commentContent: TextView = itemView.findViewById(R.id.tv_comment_content)
        var authorID: String = ""
        var delete_btn: com.google.android.material.button.MaterialButton =
            itemView.findViewById(R.id.btn_delete_comment)
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


        holder.delete_btn.visibility = View.VISIBLE


    }

    override fun getItemCount() = commentList.size
}
