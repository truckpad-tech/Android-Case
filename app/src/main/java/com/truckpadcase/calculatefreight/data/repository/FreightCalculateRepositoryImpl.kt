package com.truckpadcase.calculatefreight.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.truckpadcase.calculatefreight.data.local.AppDatabase
import com.truckpadcase.calculatefreight.data.local.HistoryDao
import com.truckpadcase.calculatefreight.domain.model.local.FreightData
import com.truckpadcase.calculatefreight.domain.repository.FreightCalculateRepository

public class FreightCalculateRepositoryImpl : FreightCalculateRepository {

    override fun saveFreightCalculateInfo(context: Context, freightData: FreightData) : Long {
        var historyDao : HistoryDao = AppDatabase.getInstance(context).routeStorageDao()
        return historyDao.insertAll(freightData)
    }

    override fun getAllFreightCalculations(context: Context) : LiveData<List<FreightData>> {
        var historyDao : HistoryDao = AppDatabase.getInstance(context).routeStorageDao()
        return historyDao.getAll()
    }

    override fun getByIdFreightCalculate(context: Context, id: Int) : LiveData<FreightData>{
        var historyDao : HistoryDao = AppDatabase.getInstance(context).routeStorageDao()
        return historyDao.geRouteStorage(id.toString())
    }

}