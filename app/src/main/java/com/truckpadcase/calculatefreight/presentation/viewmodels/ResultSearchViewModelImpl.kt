package com.truckpadcase.calculatefreight.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.data.repository.FreightCalculateRepositoryImpl
import com.truckpadcase.calculatefreight.domain.model.local.FreightData
import com.truckpadcase.calculatefreight.presentation.viewmodels.contratcs.ResultSearchViewModel

class ResultSearchViewModelImpl (application: Application) : AndroidViewModel(application), ResultSearchViewModel {

    /*-- Context --*/
    private val context = application

    private var freightCalculateRepositoryImpl: FreightCalculateRepositoryImpl = FreightCalculateRepositoryImpl()

    override fun searchData(id: Long) : LiveData<FreightData> {
        return freightCalculateRepositoryImpl.getByIdFreightCalculate(context,id.toInt())
    }

}