package dev.khalil.freightpad.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.khalil.freightpad.R
import dev.khalil.freightpad.model.UiState
import dev.khalil.freightpad.model.UiState.HISTORY
import dev.khalil.freightpad.model.UiState.INFO
import dev.khalil.freightpad.model.UiState.ROUTE
import dev.khalil.freightpad.ui.fragment.HistoryFragment
import dev.khalil.freightpad.ui.fragment.InfoFragment
import dev.khalil.freightpad.ui.fragment.RouteFragment

class CalculatorViewPagerAdapter(
  private val context: Context,
  fragmentManager: FragmentManager,
  private val states: Array<UiState>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  override fun getPageTitle(position: Int): CharSequence? {
    return when (states[position]) {
      INFO    -> context.getString(R.string.tab_info)
      ROUTE   -> context.getString(R.string.tab_route)
      HISTORY -> context.getString(R.string.tab_history)
    }
  }

  override fun getItem(position: Int): Fragment {
    return when (states[position]) {
      INFO    -> InfoFragment()
      ROUTE   -> RouteFragment()
      HISTORY -> HistoryFragment()
    }
  }

  override fun getCount(): Int = states.size
}