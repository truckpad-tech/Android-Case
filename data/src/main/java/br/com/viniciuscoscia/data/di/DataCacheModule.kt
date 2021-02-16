package br.com.viniciuscoscia.data.di

import br.com.viniciuscoscia.data.local.database.calcresults.CalcResultsDataBase
import br.com.viniciuscoscia.data.local.source.CalcResultsLocalSource
import br.com.viniciuscoscia.data.local.source.CalcResultsLocalSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val cacheDataModule = module {
    single { CalcResultsDataBase.createDataBase(androidContext()) }
    factory<CalcResultsLocalSource> { CalcResultsLocalSourceImpl(searchResultsDao = get()) }
}