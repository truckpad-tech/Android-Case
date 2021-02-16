package br.com.viniciuscoscia.truckpad.domain.usecases

import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults
import br.com.viniciuscoscia.truckpad.domain.entities.Place
import br.com.viniciuscoscia.truckpad.domain.entities.RouteCalc
import br.com.viniciuscoscia.truckpad.domain.repositories.ITruckPadRepository
import java.util.*

class GetPricesByCargoTypeUseCase(private val repository: ITruckPadRepository) :
    BaseUseCaseWithParams<CalcResults, GetPricesByCargoTypeUseCase.Params>() {

    override suspend fun execute(params: Params): CalcResults {
        with(params) {
            val prices = repository.fetchPricesByCargoType(
                axis,
                routeCalc.distance.toDouble(),
                hasReturnShipment
            )

            val calcResults = CalcResults(
                axis,
                origin.name!!,
                origin.latitude,
                origin.longitude,
                destiny.name!!,
                destiny.latitude,
                destiny.longitude,
                routeCalc.duration,
                routeCalc.durationUnit,
                routeCalc.necessaryFuel,
                routeCalc.necessaryFuelUnit,
                fuelPrice,
                routeCalc.distance,
                routeCalc.distanceUnit,
                routeCalc.tollCost,
                routeCalc.tollCostUnit,
                routeCalc.necessaryFuel,
                routeCalc.necessaryFuelUnit,
                routeCalc.totalFuelCost,
                routeCalc.totalFuelCostUnit,
                routeCalc.totalCost,
                prices.frigorificada,
                prices.geral,
                prices.granel,
                prices.neogranel,
                prices.perigosa,
                Date().time
            )

            repository.saveCalcResults(calcResults)

            return calcResults
        }
    }

    data class Params(
        val axis: Int,
        val routeCalc: RouteCalc,
        val hasReturnShipment: Boolean,
        val fuelPrice: Double,
        val origin: Place,
        val destiny: Place
    )
}