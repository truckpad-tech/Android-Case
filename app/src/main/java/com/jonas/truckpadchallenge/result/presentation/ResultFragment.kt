package com.jonas.truckpadchallenge.result.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.core.utils.gone
import com.jonas.truckpadchallenge.core.utils.visible
import com.jonas.truckpadchallenge.result.presentation.MapsActivity.Companion.SEARCH_RESULT
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import kotlinx.android.synthetic.main.fragment_result.bulk
import kotlinx.android.synthetic.main.fragment_result.dangerous
import kotlinx.android.synthetic.main.fragment_result.destination
import kotlinx.android.synthetic.main.fragment_result.distance
import kotlinx.android.synthetic.main.fragment_result.duration
import kotlinx.android.synthetic.main.fragment_result.fuel_cost
import kotlinx.android.synthetic.main.fragment_result.fuel_usage
import kotlinx.android.synthetic.main.fragment_result.general
import kotlinx.android.synthetic.main.fragment_result.group_toll
import kotlinx.android.synthetic.main.fragment_result.has_tolls
import kotlinx.android.synthetic.main.fragment_result.neogranel
import kotlinx.android.synthetic.main.fragment_result.origin
import kotlinx.android.synthetic.main.fragment_result.refrigerated
import kotlinx.android.synthetic.main.fragment_result.toll_cost
import kotlinx.android.synthetic.main.fragment_result.toll_count
import kotlinx.android.synthetic.main.fragment_result.total

class ResultFragment : Fragment() {

    private lateinit var searchResult: SearchResult

    companion object {
        fun newInstance(searchResult: SearchResult) =
            ResultFragment().apply {
                arguments = Bundle().apply { putSerializable(SEARCH_RESULT, searchResult) }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            searchResult = it.getSerializable(SEARCH_RESULT) as SearchResult
        }
        showResultInfo()
    }

    private fun showResultInfo() {
        val costUnit = searchResult.fuelCostUnit
        val tollsString = getString(R.string.result_tolls)

        origin.text = searchResult.originAddress.toString()
        destination.text = searchResult.destinationAddress.toString()
        distance.text = searchResult.distance.toString().plus(" " + searchResult.distanceUnit)
        duration.text = searchResult.duration.toString().plus(" " + searchResult.durationUnit)
        fuel_usage.text = searchResult.fuelUsage.toString().plus(" " + searchResult.fuelUsageUnit)
        fuel_cost.text = costUnit.plus(" " + searchResult.fuelCost.toString())
        total.text = costUnit.plus(" " + searchResult.totalCost.toString())

        refrigerated.text = costUnit.plus(" " + searchResult.refrigerated.toString()).plus(tollsString)
        general.text = costUnit.plus(" " + searchResult.general.toString()).plus(tollsString)
        bulk.text = costUnit.plus(" " + searchResult.bulk.toString()).plus(tollsString)
        neogranel.text = costUnit.plus(" " + searchResult.neogranel.toString()).plus(tollsString)
        dangerous.text = costUnit.plus(" " + searchResult.dangerous.toString()).plus(tollsString)

        if (searchResult.hasTolls) {
            group_toll.visible()
            has_tolls.text = getString(R.string.yes)
            toll_count.text = searchResult.tollCount.toString()
            toll_cost.text = searchResult.tollCostUnit.plus(searchResult.tollCost.toString())
        } else {
            group_toll.gone()
            has_tolls.text = getString(R.string.no)
        }
    }
}