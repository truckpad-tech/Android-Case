package com.jonas.truckpadchallenge.history.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.core.utils.LocationUtils
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import kotlinx.android.synthetic.main.search_history_item.view.destination_point_history
import kotlinx.android.synthetic.main.search_history_item.view.origin_point_history

class HistoryAdapter(
    private val list: List<SearchResult>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_history_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val origin = view.origin_point_history
        private val destination = view.destination_point_history

        fun bind(searchResult: SearchResult) {
            origin.text = getAddress(searchResult.originAddress)
            destination.text = getAddress(searchResult.destinationAddress)
        }

        private fun getAddress(location: List<Double>): String {
            val locationAddress = LocationUtils(itemView.context)
                .getLocationByLatLng(
                    latitude = location[1],
                    longitude = location[0]
                )
            return locationAddress.address
        }
    }
}

