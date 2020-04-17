package dev.khalil.freightpad.ui.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import dev.khalil.freightpad.databinding.ItemSearchLocationBinding
import dev.khalil.freightpad.model.Place

class LocationViewHolder(
  val binding: ItemSearchLocationBinding
) :
  RecyclerView.ViewHolder(binding.root) {

  fun bind(place: Place, click: LocationClick) {
    binding.placeText.text = place.displayName
    binding.root.setOnClickListener { click.locationClicked(place) }
  }
}

interface LocationClick {
  fun locationClicked(place: Place)
}