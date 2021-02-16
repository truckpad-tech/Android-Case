package br.com.viniciuscoscia.data.di

import br.com.viniciuscoscia.data.TruckPadRepository
import br.com.viniciuscoscia.truckpad.domain.repositories.ITruckPadRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule = module {
    factory<ITruckPadRepository> {
        TruckPadRepository(
            priceByCargoTypeSourceRemote = get(),
            routesCalcSourceRemote = get(),
            searchResultsLocalSource = get()
        )
    }
}

val dataModules: List<Module> = listOf(remoteDataSourceModule, repositoryModule, cacheDataModule)