package com.diegobezerra.truckpadcase

import androidx.lifecycle.ViewModelProvider
import com.diegobezerra.truckpadcase.data.api.geo.provideGeoService
import com.diegobezerra.truckpadcase.data.api.recalculafrete.provideRecalculaFreteService
import com.diegobezerra.truckpadcase.data.api.tictac.provideTictacService
import com.diegobezerra.truckpadcase.data.database.AppDatabase
import com.diegobezerra.truckpadcase.data.repository.antt.AnttRepository
import com.diegobezerra.truckpadcase.data.repository.antt.MainAnttRepository
import com.diegobezerra.truckpadcase.data.repository.autocomplete.AutocompleteRepository
import com.diegobezerra.truckpadcase.data.repository.autocomplete.MainAutocompleteRepository
import com.diegobezerra.truckpadcase.data.repository.history.HistoryRepository
import com.diegobezerra.truckpadcase.data.repository.history.MainHistoryRepository
import com.diegobezerra.truckpadcase.data.repository.route.GeoRepository
import com.diegobezerra.truckpadcase.data.repository.route.MainGeoRepository
import com.diegobezerra.truckpadcase.ui.main.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {

    single { AppDatabase.buildDatabase(get()) }

    // DAOs
    single { get<AppDatabase>().historyDao() }

    // Data Services
    single { provideGeoService() }
    single { provideTictacService() }
    single { provideRecalculaFreteService() }

}

val repositoryModule = module {

    single<AutocompleteRepository> {
        MainAutocompleteRepository(get())
    }

    single<GeoRepository> {
        MainGeoRepository(get())
    }

    single<AnttRepository> {
        MainAnttRepository(get())
    }

    single<HistoryRepository> {
        MainHistoryRepository(get())
    }
}

val appModule = module {

    // View Models
    factory { ViewModelProvider.AndroidViewModelFactory(androidApplication()) }

    viewModel {
        MainViewModel(get(), get(), get(), get())
    }

}