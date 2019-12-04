package com.diegobezerra.truckpadcase.ui.main.calculator.history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.diegobezerra.truckpadcase.R
import com.diegobezerra.truckpadcase.databinding.FragmentHistoryBinding
import com.diegobezerra.truckpadcase.ui.main.MainViewModel
import com.diegobezerra.truckpadcase.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class HistoryFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter(mainViewModel, viewLifecycleOwner)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mainViewModel
        }

        binding.recyclerView.apply {
            adapter = historyAdapter
            setHasFixedSize(true)
        }

        mainViewModel.history.observe(viewLifecycleOwner, Observer {
            historyAdapter.history = it
        })

        mainViewModel.clearHistoryAction.observe(viewLifecycleOwner, EventObserver {
            showClearHistoryDialog()
        })

        return binding.root
    }

    private fun showClearHistoryDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setMessage(R.string.history_dialog_content)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    mainViewModel.onClearHistory()
                }
                setNegativeButton(R.string.cancel) { _, _ ->
                    // No-op.
                }
            }.create().show()
        }
    }


}
