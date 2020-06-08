package com.jonas.truckpadchallenge.history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonas.truckpadchallenge.core.utils.observeOnMain
import com.jonas.truckpadchallenge.core.utils.subscribeOnIO
import com.jonas.truckpadchallenge.history.data.HistoryRepository
import io.reactivex.disposables.CompositeDisposable

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _uiState by lazy { MutableLiveData<HistoryUiState>() }
    val uiState: LiveData<HistoryUiState> get() = _uiState

    fun getSearchHistory() {
        compositeDisposable.add(
            repository.getHistory()
                .subscribeOnIO()
                .observeOnMain()
                .doOnSubscribe { _uiState.toLoading() }
                .subscribe({ result ->
                    _uiState.toSuccess(result)
                }, { error ->
                    _uiState.toError(error)
                }, {
                    _uiState.toEmpty()
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}