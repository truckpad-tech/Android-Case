package com.jonas.truckpadchallenge.history.data

import com.jonas.truckpadchallenge.core.data.database.HistoryDao
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import io.reactivex.Maybe

class HistoryRepositoryImpl(private val dao: HistoryDao) : HistoryRepository {

    override fun getHistory(): Maybe<List<SearchResult>> {
        return dao.getAll()
            .flatMap { resultEntity ->
                if (resultEntity.isNotEmpty()) {
                    Maybe.just(resultEntity)
                } else {
                    Maybe.empty()
                }
            }.doOnError { Throwable("There was an error retrieving history") }
    }

    override fun saveHistory(searchResult: SearchResult) {
        dao.insertResult(searchResult)
    }
}