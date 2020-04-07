package com.truckpadcase.calculatefreight.presentation.viewmodels.contratcs

import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.domain.model.local.FreightData

interface HistorySearchViewModel {
    fun getHistory() : LiveData<List<FreightData>>
}