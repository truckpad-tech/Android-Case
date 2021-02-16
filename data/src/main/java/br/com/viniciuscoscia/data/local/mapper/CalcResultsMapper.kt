package br.com.viniciuscoscia.data.local.mapper

import br.com.viniciuscoscia.data.local.model.CalcResultsEntity
import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults

object CalcResultsMapper {
    fun mapCalcResultsEntity(searchResultsEntity: List<CalcResultsEntity>) =
        searchResultsEntity.map { CalcResultsMapper.map(it) }

    fun map(searchResultsEntity: CalcResultsEntity) = CalcResults(
        axis = searchResultsEntity.axis,
        placeOfOriginName = searchResultsEntity.placeOfOriginName,
        placeOfOriginLatitude = searchResultsEntity.placeOfOriginLatitude,
        placeOfOriginLongitude = searchResultsEntity.placeOfOriginLongitude,
        placeOfDestinyName = searchResultsEntity.placeOfDestinyName,
        placeOfDestinyLatitude = searchResultsEntity.placeOfDestinyLatitude,
        placeOfDestinyLongitude = searchResultsEntity.placeOfDestinyLongitude,
        duration = searchResultsEntity.duration,
        durationUnit = searchResultsEntity.durationUnit,
        fuelConsumption = searchResultsEntity.fuelConsumption,
        fuelUnity = searchResultsEntity.fuelUnity,
        fuelPrice = searchResultsEntity.fuelPrice,
        distance = searchResultsEntity.distance,
        distanceUnit = searchResultsEntity.distanceUnit,
        tollCost = searchResultsEntity.tollCost,
        tollCostUnit = searchResultsEntity.tollCostUnit,
        necessaryFuel = searchResultsEntity.necessaryFuel,
        necessaryFuelUnity = searchResultsEntity.necessaryFuelUnity,
        totalFuelCost = searchResultsEntity.totalFuelCost,
        totalFuelCostUnit = searchResultsEntity.totalFuelCostUnit,
        totalCost = searchResultsEntity.totalCost,
        frigorificada = searchResultsEntity.frigorificada,
        geral = searchResultsEntity.geral,
        granel = searchResultsEntity.granel,
        neogranel = searchResultsEntity.neogranel,
        perigosa = searchResultsEntity.perigosa,
        timeStamp = searchResultsEntity.timeStamp
    )

    fun mapCalcResults(calcResults: List<CalcResults>) =
        calcResults.map { CalcResultsMapper.map(it) }

    fun map(calcResults: CalcResults) = CalcResultsEntity(
        axis = calcResults.axis,
        placeOfOriginName = calcResults.placeOfOriginName,
        placeOfOriginLatitude = calcResults.placeOfOriginLatitude,
        placeOfOriginLongitude = calcResults.placeOfOriginLongitude,
        placeOfDestinyName = calcResults.placeOfDestinyName,
        placeOfDestinyLatitude = calcResults.placeOfDestinyLatitude,
        placeOfDestinyLongitude = calcResults.placeOfDestinyLongitude,
        duration = calcResults.duration,
        durationUnit = calcResults.durationUnit,
        fuelConsumption = calcResults.fuelConsumption,
        fuelUnity = calcResults.fuelUnity,
        fuelPrice = calcResults.fuelPrice,
        distance = calcResults.distance,
        distanceUnit = calcResults.distanceUnit,
        tollCost = calcResults.tollCost,
        tollCostUnit = calcResults.tollCostUnit,
        necessaryFuel = calcResults.necessaryFuel,
        necessaryFuelUnity = calcResults.necessaryFuelUnity,
        totalFuelCost = calcResults.totalFuelCost,
        totalFuelCostUnit = calcResults.totalFuelCostUnit,
        totalCost = calcResults.totalCost,
        frigorificada = calcResults.frigorificada,
        geral = calcResults.geral,
        granel = calcResults.granel,
        neogranel = calcResults.neogranel,
        perigosa = calcResults.perigosa,
        timeStamp = calcResults.timeStamp
    )
}