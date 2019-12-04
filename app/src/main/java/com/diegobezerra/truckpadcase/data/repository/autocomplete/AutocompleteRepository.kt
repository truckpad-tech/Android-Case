package com.diegobezerra.truckpadcase.data.repository.autocomplete

import com.diegobezerra.truckpadcase.data.api.recalculafrete.RecalculaFreteService
import com.diegobezerra.truckpadcase.domain.model.Place
import com.diegobezerra.truckpadcase.util.Result
import com.diegobezerra.truckpadcase.util.safeRequest

interface AutocompleteRepository {

    suspend fun getAutocomplete(search: String): Result<List<Place>>

}

class MainAutocompleteRepository(
    val recalculaFreteService: RecalculaFreteService
) : AutocompleteRepository {

    override suspend fun getAutocomplete(search: String): Result<List<Place>> {
        return when (val result =
            safeRequest(call = { recalculaFreteService.autocomplete(search) })) {
            is Result.Success -> {
                Result.Success(result.data.places)
            }
            else -> Result.Error
        }
    }

}