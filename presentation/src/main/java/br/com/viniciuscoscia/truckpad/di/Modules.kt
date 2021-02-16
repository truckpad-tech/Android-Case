package br.com.viniciuscoscia.truckpad.di

import br.com.viniciuscoscia.truckpad.ui.main.fragment.home.HomeViewModel
import br.com.viniciuscoscia.truckpad.ui.main.fragment.routeresultsfragment.RouteResultsViewModel
import br.com.viniciuscoscia.truckpad.ui.main.fragment.routeshistory.RoutesHistoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module(override = true) {
    viewModel {
        HomeViewModel(androidContext(),
                getRouteCalcsUseCase = get(),
                getPricesByCargoTypeUseCase = get())
    }

    viewModel {
        RouteResultsViewModel()
    }

    viewModel {
        RoutesHistoryViewModel(get())
    }
}