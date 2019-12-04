package com.diegobezerra.truckpadcase.ui.main.calculator.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diegobezerra.truckpadcase.databinding.FragmentResultsBinding
import com.diegobezerra.truckpadcase.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResultsFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()

    private lateinit var binding: FragmentResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mainViewModel
        }
        return binding.root
    }
}
