package com.jonas.truckpadchallenge.core.di

import androidx.lifecycle.ViewModelProvider
import com.jonas.truckpadchallenge.search.presentation.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    factory { ViewModelProvider.AndroidViewModelFactory(androidApplication()) }
    viewModel { SearchViewModel() }
}