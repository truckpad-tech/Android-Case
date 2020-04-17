package dev.khalil.freightpad.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.khalil.freightpad.R
import dev.khalil.freightpad.databinding.ItemSearchLocationBinding
import dev.khalil.freightpad.model.Place
import dev.khalil.freightpad.ui.adapter.holder.LocationClick
import dev.khalil.freightpad.ui.adapter.holder.LocationViewHolder

class LocationsAdapter(
  private val locations: ArrayList<Place>,
  private val placeClick: LocationClick
) :
  RecyclerView.Adapter<LocationViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
    val binding = DataBindingUtil.inflate<ItemSearchLocationBinding>(
      LayoutInflater.from(parent.context),
      R.layout.item_search_location,
      parent,
      false
    )

    return LocationViewHolder(binding)
  }

  override fun getItemCount() = locations.size

  override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
    holder.bind(locations[position], placeClick)
  }

  fun updateLocations(locations: List<Place>) {
    this.locations.clear()
    this.locations.addAll(locations)
    notifyDataSetChanged()
  }
}