package com.jonas.truckpadchallenge.maps.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.history.HistoryFragment
import com.jonas.truckpadchallenge.result.ResultFragment
import com.jonas.truckpadchallenge.search.presentation.SearchFragment
import kotlinx.android.synthetic.main.fragment_info_input.view.bottom_navigation_info_input

class InfoInputFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val searchFragment = SearchFragment()
    private val resultFragment = ResultFragment()
    private val historyFragment = HistoryFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_input, container, false)

        view.bottom_navigation_info_input.setOnNavigationItemSelectedListener(this)

        showFragment(searchFragment)

        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> showFragment(searchFragment)
            R.id.result -> showFragment(resultFragment)
            R.id.history -> showFragment(historyFragment)
        }
        item.isChecked = true
        return true
    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_input_view, fragment)
            .addToBackStack(null)
            .commit()
    }
}