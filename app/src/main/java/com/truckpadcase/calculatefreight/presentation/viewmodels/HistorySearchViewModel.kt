package com.truckpadcase.calculatefreight.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.data.repository.FreightCalculateRepositoryImpl
import com.truckpadcase.calculatefreight.domain.model.local.FreightData

class HistorySearchViewModel (application: Application) : AndroidViewModel(application) {
    /*-- Context --*/
    private val context = application

    private var freightCalculateRepositoryImpl: FreightCalculateRepositoryImpl = FreightCalculateRepositoryImpl()

    fun getHistory() : LiveData<List<FreightData>>{
        return freightCalculateRepositoryImpl.getAllFreightCalculations(context)
    }

}