package com.jonas.truckpadchallenge.history.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.core.utils.gone
import com.jonas.truckpadchallenge.core.utils.visible
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Empty
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Error
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Loading
import com.jonas.truckpadchallenge.history.presentation.HistoryUiState.Success
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import kotlinx.android.synthetic.main.fragment_history.empty_history_text_view
import kotlinx.android.synthetic.main.fragment_history.history_progress_bar
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
        if (isShow) history_progress_bar.visible() else history_progress_bar.gone()
    }

    private fun onEmpty() {
        history_recycler.gone()
        empty_history_text_view.visible()
    }

    private fun onSuccess(list: List<SearchResult>) {
        empty_history_text_view.gone()
        history_recycler.apply {
            visible()
            adapter = HistoryAdapter(list)
        }
    }

    private fun onError(throwable: Throwable) {
        empty_history_text_view.gone()
        history_recycler.gone()
        showAlertDialog()
    }

    private fun showAlertDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle(R.string.attention_dialog_title)
                setMessage(R.string.generic_error_dialog_message)
                setPositiveButton(android.R.string.ok, null)
            }.create().show()
        }
    }
}