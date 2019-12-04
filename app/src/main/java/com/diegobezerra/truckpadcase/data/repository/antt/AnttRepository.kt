package com.diegobezerra.truckpadcase.data.repository.antt

import com.diegobezerra.truckpadcase.data.api.tictac.AnttPricesBody
import com.diegobezerra.truckpadcase.data.api.tictac.TictacService
import com.diegobezerra.truckpadcase.domain.model.AnttPrices
import com.diegobezerra.truckpadcase.util.Result
import com.diegobezerra.truckpadcase.util.safeRequest

interface AnttRepository {

    suspend fun getPrices(body: AnttPricesBody): Result<AnttPrices>

}

class MainAnttRepository(
    val tictacService: TictacService
) : AnttRepository {

    private val cache: HashMap<String, AnttPrices> = hashMapOf()

    override suspend fun getPrices(body: AnttPricesBody): Result<AnttPrices> {
        val id = getAnttPricesBodyId(body)
        val cached = cache[id]
        if (cached != null) {
            return Result.Success(cached)
        }
        return when (val result = safeRequest(call = { tictacService.getAnttPrices(body) })) {
            is Result.Success -> {
                cache[id] = result.data
                result
            }
            else -> result
        }
    }

    private fun getAnttPricesBodyId(body: AnttPricesBody): String {
        return "${body.axis}-${body.distance}-${body.has_return_shipment}"
    }


}