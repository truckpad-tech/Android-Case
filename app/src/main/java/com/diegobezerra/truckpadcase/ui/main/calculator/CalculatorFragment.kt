package com.diegobezerra.truckpadcase.ui.main.calculator


import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import com.diegobezerra.truckpadcase.R
import com.diegobezerra.truckpadcase.databinding.FragmentCalculatorBinding
import com.diegobezerra.truckpadcase.ui.main.MainViewModel
import com.diegobezerra.truckpadcase.ui.main.UiState
import com.diegobezerra.truckpadcase.ui.main.UiState.*
import com.diegobezerra.truckpadcase.ui.main.calculator.history.HistoryFragment
import com.diegobezerra.truckpadcase.ui.main.calculator.results.ResultsFragment
import com.diegobezerra.truckpadcase.ui.main.calculator.search.SearchFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel


class CalculatorFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentCalculatorBinding

    private var expandIconAnimator: ValueAnimator? = null
    private var behavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = getSharedViewModel()
        binding.viewModel = mainViewModel

        binding.viewPager.apply {
            val calculatorAdapter = CalculatorAdapter(childFragmentManager, values())
            adapter = calculatorAdapter
            offscreenPageLimit = calculatorAdapter.count
            binding.tabs.setupWithViewPager(this)
        }

        behavior = BottomSheetBehavior.from(binding.sheet)
        behavior?.bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            var previousState: Int = STATE_COLLAPSED

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (updateExpandIcon(previousState, newState)) {
                    previousState = newState
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // No-op.
            }
        }

        mainViewModel.toggleSheetAction.observe(viewLifecycleOwner, Observer {
            toggleSheet()
        })

        mainViewModel.state.observe(viewLifecycleOwner, Observer {
            // Sync view pager with state
            binding.viewPager.setCurrentItem(it.ordinal, true)
        })
    }

    fun onBackPressed(): Boolean {
        if (behavior?.state == STATE_EXPANDED) {
            behavior?.state = STATE_COLLAPSED
            return true
        }
        return false
    }

    private fun toggleSheet() {
        behavior?.let {
            when (it.state) {
                STATE_EXPANDED -> it.state = STATE_COLLAPSED
                STATE_COLLAPSED -> it.state = STATE_EXPANDED
                else -> Unit // No-op
            }
        }
    }

    private fun updateExpandIcon(previousState: Int, newState: Int): Boolean {
        return if (newState == STATE_COLLAPSED || newState == STATE_EXPANDED) {
            if (newState == previousState) {
                false
            } else {
                when (newState) {
                    STATE_COLLAPSED -> {
                        animateExpandIcon(-1f, 1f)
                    }
                    STATE_EXPANDED -> {
                        animateExpandIcon(1f, -1f)
                    }
                    else -> Unit
                }
                true
            }
        } else {
            false
        }
    }

    private fun animateExpandIcon(start: Float, end: Float) {
        expandIconAnimator?.cancel()
        expandIconAnimator = ValueAnimator.ofFloat(start, end).apply {
            addUpdateListener {
                binding.expandIcon.scaleY = animatedValue as Float
            }
            start()
        }
    }

    inner class CalculatorAdapter(
        fm: FragmentManager,
        private val states: Array<UiState>
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getPageTitle(position: Int): CharSequence? {
            return when (states[position]) {
                SEARCH -> {
                    getString(R.string.calculator_tab_search)
                }
                RESULTS -> {
                    getString(R.string.calculator_tab_results)
                }
                HISTORY -> {
                    getString(R.string.calculator_tab_history)
                }
            }
        }

        override fun getItem(position: Int): Fragment {
            return when (states[position]) {
                SEARCH -> {
                    SearchFragment()
                }
                RESULTS -> {
                    ResultsFragment()
                }
                HISTORY -> {
                    HistoryFragment()
                }
            }
        }

        override fun getCount(): Int = states.size
    }
}
