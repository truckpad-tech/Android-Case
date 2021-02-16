package br.com.viniciuscoscia.truckpad.domain.entities

data class PriceByCargoType (
        val frigorificada: Double,
        val geral: Double,
        val granel: Double,
        val neogranel: Double,
        val perigosa: Double
)