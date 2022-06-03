package com.jc.locationproject.ui.locations


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jc.locationproject.R
import com.jc.locationproject.database.LocationLog

class LocationsAdapter(): RecyclerView.Adapter<LocationViewHolder>()  {

    var data = listOf<LocationLog>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.location_item_view, parent, false) as CardView

        return LocationViewHolder(view)
    }
}