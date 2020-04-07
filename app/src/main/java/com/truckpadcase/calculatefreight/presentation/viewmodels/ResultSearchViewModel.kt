package com.truckpadcase.calculatefreight.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.data.repository.FreightCalculateRepositoryImpl
import com.truckpadcase.calculatefreight.domain.model.local.FreightData

class ResultSearchViewModel (application: Application) : AndroidViewModel(application)  {

    /*-- Context --*/
    private val context = application

    private var freightCalculateRepositoryImpl: FreightCalculateRepositoryImpl = FreightCalculateRepositoryImpl()

    fun searchData(id: Long) : LiveData<FreightData> {
        return freightCalculateRepositoryImpl.getByIdFreightCalculate(context,id.toInt())
    }

}