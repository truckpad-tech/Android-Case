package com.truckpadcase.calculatefreight.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.data.repository.FreightCalculateRepositoryImpl
import com.truckpadcase.calculatefreight.domain.model.local.FreightData
import com.truckpadcase.calculatefreight.presentation.viewmodels.contratcs.HistorySearchViewModel

class HistorySearchViewModelImpl (application: Application) : AndroidViewModel(application),
    HistorySearchViewModel {

    /*-- Context --*/
    private val context = application

    private var freightCalculateRepositoryImpl: FreightCalculateRepositoryImpl = FreightCalculateRepositoryImpl()

    override fun getHistory() : LiveData<List<FreightData>>{
        return freightCalculateRepositoryImpl.getAllFreightCalculations(context)
    }

}