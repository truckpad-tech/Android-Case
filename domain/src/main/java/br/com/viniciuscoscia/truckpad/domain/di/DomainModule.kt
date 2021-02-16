package br.com.viniciuscoscia.truckpad.domain.di

import br.com.viniciuscoscia.truckpad.domain.usecases.FindCalcResultsHistoryUseCase
import br.com.viniciuscoscia.truckpad.domain.usecases.GetPricesByCargoTypeUseCase
import br.com.viniciuscoscia.truckpad.domain.usecases.GetRouteCalcsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetRouteCalcsUseCase(get()) }
    factory { GetPricesByCargoTypeUseCase(get()) }
    factory { FindCalcResultsHistoryUseCase(get()) }
}

val domainModule = listOf(useCaseModule)