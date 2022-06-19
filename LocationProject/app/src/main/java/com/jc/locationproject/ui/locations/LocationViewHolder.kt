package com.jc.locationproject.ui.locations


import android.annotation.SuppressLint
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jc.locationproject.R
import com.jc.locationproject.database.LocationLog
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class LocationViewHolder(private val itemView: CardView): RecyclerView.ViewHolder(itemView) {
    private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
    private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    private val displacementTextView: TextView = itemView.findViewById(R.id.displacementTextView)
    private val velocityTextView: TextView = itemView.findViewById(R.id.velocityTextView)
    private val intervalTextView: TextView = itemView.findViewById(R.id.intervalTextView)
    private val stoppedTimeTextView: TextView = itemView.findViewById(R.id.stoppedTimeTextView)

    @SuppressLint("SetTextI18n")
    fun setData(location: LocationLog) {
        locationTextView.text = location.lat.toString() + " | " + location.lon.toString()
        dateTextView.text = getFormattedDate(location)
        displacementTextView.text = getFormattedDouble(location.displacement ?: 0.0) + " metros"
        velocityTextView.text = getFormattedDouble(location.velocity ?: 0.0) + " m/s"
        intervalTextView.text = "Interval: ${getFormattedDouble((location.interval ?: 0.0) / 60)} minutes"
        stoppedTimeTextView.text = "Stopped: ${getFormattedDouble((location.stoppedTime ?: 0.0) / 60)} minutes"
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(location: LocationLog): String {
        val date = Date(location.createdOn ?: 0)
        val format = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        return format.format(date)
    }

    private fun getFormattedDouble(value: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(value).toString()
    }
}