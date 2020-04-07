package com.truckpadcase.calculatefreight.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.domain.model.local.FreightData

interface FreightCalculateRepository {

    fun saveFreightCalculateInfo( context: Context, freightData: FreightData) : Long

    fun getAllFreightCalculations(context: Context) : LiveData<List<FreightData>>

    fun getByIdFreightCalculate(context: Context, id : Int) : LiveData<FreightData>

}