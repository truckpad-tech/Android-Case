package com.jonas.truckpadchallenge.history.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Empty
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Error
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Loading
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Success
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import kotlinx.android.synthetic.main.fragment_history.history_recycler
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        getHistorySearch()
    }

    private fun setupRecyclerView() {
        history_recycler.layoutManager = LinearLayoutManager(context)
    }

    private fun setupViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer(::updateUI))
    }

    private fun getHistorySearch() {
        viewModel.getSearchHistory()
    }

    private fun updateUI(state: HistoryUiState) {
        toggleLoading(false)
        when (state) {
            is Loading -> toggleLoading(true)
            is Empty -> onEmpty()
            is Success -> onSuccess(state.listSearchResult)
            is Error -> onError(state.error)
        }
    }

    private fun toggleLoading(isShow: Boolean) {
        Toast.makeText(context, "Loading $isShow", Toast.LENGTH_SHORT).show()
    }

    private fun onEmpty() {
        Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(list: List<SearchResult>) {
        history_recycler.adapter = HistoryAdapter(list)
    }

    private fun onError(throwable: Throwable) {
        Toast.makeText(context, "Loading ${throwable.message}", Toast.LENGTH_SHORT).show()
    }
}