package com.jonas.truckpadchallenge.history.data

import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import io.reactivex.Maybe

interface HistoryRepository {
    fun getHistory(): Maybe<List<SearchResult>>
    fun saveHistory(searchResult: SearchResult)
}