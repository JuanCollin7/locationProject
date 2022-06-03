package com.jc.locationproject.ui.locations


import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jc.locationproject.R
import com.jc.locationproject.database.LocationLog

class LocationViewHolder(private val itemView: CardView): RecyclerView.ViewHolder(itemView) {
    private val firstTextView: TextView = itemView.findViewById(R.id.firstTextView)
    private val secondTextView: TextView = itemView.findViewById(R.id.secodTextView)

    fun setData(location: LocationLog) {
        firstTextView.text = location.lat.toString() + " | " + location.long.toString()
        secondTextView.text = location.timestamp.toString()
    }
}