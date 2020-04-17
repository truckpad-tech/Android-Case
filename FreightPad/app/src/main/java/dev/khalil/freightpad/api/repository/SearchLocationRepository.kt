package dev.khalil.freightpad.api.repository

import dev.khalil.freightpad.model.Place
import io.reactivex.Single

interface SearchLocationRepository {
  fun searchLocation(query: String): Single<List<Place>>
}