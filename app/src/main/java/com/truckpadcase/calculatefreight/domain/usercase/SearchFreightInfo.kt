package com.truckpadcase.calculatefreight.domain.usercase

import com.truckpadcase.calculatefreight.data.repository.FreightCalculateRepositoryImpl
import com.truckpadcase.calculatefreight.domain.usercase.contracts.SearchFreightInfoContract

class SearchFreightInfo(val repositoryImpl: FreightCalculateRepositoryImpl) :
    SearchFreightInfoContract {

}