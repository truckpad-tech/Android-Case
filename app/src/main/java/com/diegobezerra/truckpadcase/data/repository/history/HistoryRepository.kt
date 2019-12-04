package com.diegobezerra.truckpadcase.data.repository.history

import androidx.lifecycle.LiveData
import com.diegobezerra.truckpadcase.data.database.dao.HistoryDao
import com.diegobezerra.truckpadcase.domain.model.HistoryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface HistoryRepository {

    suspend fun clear()

    suspend fun insert(entry: HistoryEntry)

    fun getHistory(): LiveData<List<HistoryEntry>>

}

class MainHistoryRepository(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override suspend fun clear() = withContext(Dispatchers.IO) {
        historyDao.clear()
    }

    override suspend fun insert(entry: HistoryEntry) = withContext(Dispatchers.IO) {
        historyDao.insert(entry)
    }

    override fun getHistory(): LiveData<List<HistoryEntry>> = historyDao.getAll()

}