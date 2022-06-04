package com.jc.locationproject.ui.locations


import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jc.locationproject.R
import com.jc.locationproject.database.LocationLog
import java.text.SimpleDateFormat
import java.util.*

class LocationViewHolder(private val itemView: CardView): RecyclerView.ViewHolder(itemView) {
    private val firstTextView: TextView = itemView.findViewById(R.id.firstTextView)
    private val secondTextView: TextView = itemView.findViewById(R.id.secodTextView)

    fun setData(location: LocationLog) {
        firstTextView.text = location.lat.toString() + " | " + location.lon.toString()
        secondTextView.text = getFormattedDate(location)
    }

    private fun getFormattedDate(location: LocationLog): String {
        val date = Date(location.timestamp ?: 0)
        val format = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        return format.format(date)
    }
}