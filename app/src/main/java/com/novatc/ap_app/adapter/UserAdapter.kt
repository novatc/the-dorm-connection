package com.novatc.ap_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.model.User

class UserAdapter(val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var userName: TextView = itemView.findViewById(R.id.tv_user_name)
        var userDorm: TextView = itemView.findViewById(R.id.tv_user_dorm)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        val userItem = userList[position]
        holder.userDorm.text = userItem.userDorm
        holder.userName.text = userItem.username
    }

    override fun getItemCount() = userList.size
}