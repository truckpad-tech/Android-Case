package com.truckpadcase.calculatefreight.presentation.viewmodels.contratcs

import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.domain.model.local.FreightData

interface ResultSearchViewModel {

    fun searchData(id: Long) : LiveData<FreightData>
}