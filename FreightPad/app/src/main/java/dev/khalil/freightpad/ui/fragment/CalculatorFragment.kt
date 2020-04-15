package dev.khalil.freightpad.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dev.khalil.freightpad.R
import dev.khalil.freightpad.databinding.FragmentCalculatorBinding
import dev.khalil.freightpad.model.UiState
import dev.khalil.freightpad.ui.adapter.CalculatorViewPagerAdapter

class CalculatorFragment : Fragment() {

  private lateinit var binding: FragmentCalculatorBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator, container, false)

    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    binding.viewPager.apply {
      val viewPagerAdapter =
        CalculatorViewPagerAdapter(this.context, childFragmentManager, UiState.values())
      adapter = viewPagerAdapter
      offscreenPageLimit = viewPagerAdapter.count
      binding.tabLayout.setupWithViewPager(this)
    }
  }
}