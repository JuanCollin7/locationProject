package com.jc.locationproject.ui.admin

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jc.locationproject.R
import com.jc.locationproject.models.User
import java.text.SimpleDateFormat
import java.util.*

class UserViewHolder(private val itemView: CardView): RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val latTextView: TextView = itemView.findViewById(R.id.latTextView)
    private val lonTextView: TextView = itemView.findViewById(R.id.lonTextView)
    private val dispTextView: TextView = itemView.findViewById(R.id.dispTextView)
    private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

    @SuppressLint("SetTextI18n")
    fun setData(user: User) {
        nameTextView.text = user.name
        latTextView.text = "Lat: " + user.lastLat
        lonTextView.text = "Lon: " + user.lastLat
        dispTextView.text = "Displacement: " + user.displacement
        dateTextView.text = "Last Updated on: " + getFormattedDate(user.lastTimestamp)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(timestamp: Long): String {
        val date = Date(timestamp ?: 0)
        val format = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        return format.format(date)
    }
}