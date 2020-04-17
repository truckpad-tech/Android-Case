package dev.khalil.freightpad.api.repository

import dev.khalil.freightpad.api.service.SearchLocationService
import dev.khalil.freightpad.model.Place
import io.reactivex.Single

class SearchLocationRepositoryImpl(private val api: SearchLocationService) :
  SearchLocationRepository {

  override fun searchLocation(query: String): Single<List<Place>> {
    return api.searchLocation(query).map { it.places }
  }
}