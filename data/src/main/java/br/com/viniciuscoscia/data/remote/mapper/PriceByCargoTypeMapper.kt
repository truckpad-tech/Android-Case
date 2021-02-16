package br.com.viniciuscoscia.data.remote.mapper

import br.com.viniciuscoscia.data.remote.model.pricebycargotype.PriceByCargoTypeResponse
import br.com.viniciuscoscia.truckpad.domain.entities.PriceByCargoType

object PriceByCargoTypeMapper {
    fun map(priceByCargoTypeResponse: PriceByCargoTypeResponse) = PriceByCargoType(
            frigorificada = priceByCargoTypeResponse.frigorificada,
            geral = priceByCargoTypeResponse.geral,
            granel = priceByCargoTypeResponse.granel,
            neogranel = priceByCargoTypeResponse.neogranel,
            perigosa = priceByCargoTypeResponse.perigosa
    )
}