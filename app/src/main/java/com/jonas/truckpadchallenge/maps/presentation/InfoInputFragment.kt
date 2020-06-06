package com.jonas.truckpadchallenge.maps.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.history.HistoryFragment
import com.jonas.truckpadchallenge.result.ResultFragment
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import com.jonas.truckpadchallenge.search.presentation.SearchFragment
import kotlinx.android.synthetic.main.fragment_info_input.bottom_navigation_info_input
import kotlinx.android.synthetic.main.fragment_info_input.view.bottom_navigation_info_input
import java.io.Serializable

class InfoInputFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val searchFragment = SearchFragment()
    private var resultFragment = ResultFragment.newInstance()
    private val historyFragment = HistoryFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_input, container, false)

        view.bottom_navigation_info_input.setOnNavigationItemSelectedListener(this)

        showFragment(searchFragment)
        setupListenerResultFragment()

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

    private fun setupListenerResultFragment() {
        childFragmentManager.setFragmentResultListener(
            REQUEST_KEY,
            this,
            FragmentResultListener { requestKey, result ->
                if (requestKey == REQUEST_KEY) onFragmentResult(result)
                else TODO("ShowErrorDialog")
            })
    }

    private fun onFragmentResult(bundle: Bundle) {
        val searchResult = bundle.getSerializable(RESULT_INFO)
        goToResultFragment(searchResult)
    }

    private fun goToResultFragment(searchResult: Serializable?) {
        resultFragment = ResultFragment.newInstance(searchResult as SearchResult)
        bottom_navigation_info_input.selectedItemId = R.id.result
        clearListenerResultFragment()
    }

    private fun clearListenerResultFragment() {
        childFragmentManager.clearFragmentResultListener(REQUEST_KEY)
    }

    companion object {
        const val REQUEST_KEY = "searchFragment"
        const val RESULT_INFO = "result"
    }
}