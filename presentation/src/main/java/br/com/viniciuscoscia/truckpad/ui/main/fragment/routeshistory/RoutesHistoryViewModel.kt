package br.com.viniciuscoscia.truckpad.ui.main.fragment.routeshistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults
import br.com.viniciuscoscia.truckpad.domain.usecases.FindCalcResultsHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RoutesHistoryViewModel(private val findCalcResultsHistoryUseCase: FindCalcResultsHistoryUseCase) :
    ViewModel() {
    private val _searchResultsHistory = MutableLiveData<List<CalcResults>>()
    fun getCalcResultsHistory(): LiveData<List<CalcResults>> {
        GlobalScope.launch(Dispatchers.Main) {
            _searchResultsHistory.value = findCalcResultsHistoryUseCase.execute()
        }

        return _searchResultsHistory
    }
}