package dev.khalil.freightpad.model

data class SearchLocationResponse(
  val places: List<Place>,
  val provider: String
)