package dev.khalil.freightpad.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import dev.khalil.freightpad.databinding.FragmentCalculatorBinding
import dev.khalil.freightpad.model.UiState
import dev.khalil.freightpad.ui.adapter.CalculatorViewPagerAdapter

class CalculatorFragment : Fragment(), ViewPagerControl {

  private lateinit var binding: FragmentCalculatorBinding
  private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    binding = FragmentCalculatorBinding.inflate(inflater, container, false)

    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    initViewPager()
    initBottomSheet()
  }

  fun collapseBottomSheet() {
    if (bottomSheetBehavior.state == STATE_EXPANDED) {
      bottomSheetBehavior.state = STATE_COLLAPSED
    }
  }

  private fun initBottomSheet() {
    bottomSheetBehavior.state = STATE_EXPANDED
  }

  private fun initViewPager() {
    binding.viewPager.apply {
      val viewPagerAdapter =
        CalculatorViewPagerAdapter(this.context, childFragmentManager, UiState.values())
      adapter = viewPagerAdapter
      offscreenPageLimit = viewPagerAdapter.count
      binding.tabLayout.setupWithViewPager(this)
    }
  }

  override fun setPage(state: UiState) {
    binding.viewPager.currentItem = UiState.values().indexOf(state)
  }

}