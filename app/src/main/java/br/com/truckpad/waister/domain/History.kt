package br.com.truckpad.waister.domain

import br.com.truckpad.waister.api.ANTTPrice
import br.com.truckpad.waister.api.Calculate

data class History(
    val origin: String = "",
    val destination: String = "",
    val axis: String = "",
    val calculate: Calculate,
    val anttPrice: ANTTPrice
)