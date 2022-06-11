package com.jc.locationproject.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jc.locationproject.R
import com.jc.locationproject.models.User

class UsersAdapter(): RecyclerView.Adapter<UserViewHolder>()  {

    var data = listOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.user_item_view, parent, false) as CardView

        return UserViewHolder(view)
    }
}