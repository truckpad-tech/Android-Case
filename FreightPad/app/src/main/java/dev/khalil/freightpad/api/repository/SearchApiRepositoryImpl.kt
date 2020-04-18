package dev.khalil.freightpad.api.repository

import dev.khalil.freightpad.api.service.SearchApiService
import dev.khalil.freightpad.model.Place
import io.reactivex.Single

class SearchApiRepositoryImpl(private val api: SearchApiService) :
  SearchApiRepository {

  override fun searchLocation(query: String): Single<List<Place>> {
    return api.searchLocation(query).map { it.places }
  }
}