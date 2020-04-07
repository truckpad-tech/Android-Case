package com.truckpadcase.calculatefreight.domain.usercase

import com.truckpadcase.calculatefreight.domain.repository.FreightCalculateRepository
import com.truckpadcase.calculatefreight.domain.usercase.contracts.ShowFreightHistoryContract

class ShowFreightHistory(val freightCalculateRepository: FreightCalculateRepository ) :
    ShowFreightHistoryContract {


    override fun invoke() {
    }
}