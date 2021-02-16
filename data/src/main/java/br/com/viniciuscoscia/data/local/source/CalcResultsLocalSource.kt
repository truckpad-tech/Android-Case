package br.com.viniciuscoscia.data.local.source

import br.com.viniciuscoscia.data.local.database.calcresults.CalcResultsDAO
import br.com.viniciuscoscia.data.local.model.CalcResultsEntity

interface CalcResultsLocalSource {
    suspend fun getCalcResults(): List<CalcResultsEntity>
    suspend fun insertData(searchResultsEntity: CalcResultsEntity)
    suspend fun updateData(searchResultsEntity: CalcResultsEntity)
    suspend fun deleteData(searchResultsEntity: CalcResultsEntity)
    suspend fun findById(id: Int): CalcResultsEntity
}

class CalcResultsLocalSourceImpl(private val searchResultsDao: CalcResultsDAO) :
    CalcResultsLocalSource {

    override suspend fun getCalcResults(): List<CalcResultsEntity> =
        searchResultsDao.getCalcResults()

    override suspend fun insertData(searchResultsEntity: CalcResultsEntity) =
        searchResultsDao.insert(searchResultsEntity)

    override suspend fun updateData(searchResultsEntity: CalcResultsEntity) =
        searchResultsDao.update(searchResultsEntity)

    override suspend fun deleteData(searchResultsEntity: CalcResultsEntity) =
        searchResultsDao.delete(searchResultsEntity)

    override suspend fun findById(id: Int): CalcResultsEntity {
        return searchResultsDao.findById(id)
    }

}