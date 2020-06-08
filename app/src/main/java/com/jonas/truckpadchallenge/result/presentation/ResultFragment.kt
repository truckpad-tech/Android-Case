package com.jonas.truckpadchallenge.result.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.core.utils.visible
import com.jonas.truckpadchallenge.result.presentation.MapsActivity.Companion.SEARCH_RESULT
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import kotlinx.android.synthetic.main.activity_result.bulk
import kotlinx.android.synthetic.main.activity_result.cached
import kotlinx.android.synthetic.main.activity_result.dangerous
import kotlinx.android.synthetic.main.activity_result.destination
import kotlinx.android.synthetic.main.activity_result.distance
import kotlinx.android.synthetic.main.activity_result.distance_unit
import kotlinx.android.synthetic.main.activity_result.duration
import kotlinx.android.synthetic.main.activity_result.duration_unit
import kotlinx.android.synthetic.main.activity_result.fuel_cost
import kotlinx.android.synthetic.main.activity_result.fuel_cost_unit
import kotlinx.android.synthetic.main.activity_result.fuel_usage
import kotlinx.android.synthetic.main.activity_result.fuel_usage_unit
import kotlinx.android.synthetic.main.activity_result.general
import kotlinx.android.synthetic.main.activity_result.group_result
import kotlinx.android.synthetic.main.activity_result.has_tolls
import kotlinx.android.synthetic.main.activity_result.neogranel
import kotlinx.android.synthetic.main.activity_result.origin
import kotlinx.android.synthetic.main.activity_result.provider
import kotlinx.android.synthetic.main.activity_result.refrigerated
import kotlinx.android.synthetic.main.activity_result.toll_cost
import kotlinx.android.synthetic.main.activity_result.toll_cost_unit
import kotlinx.android.synthetic.main.activity_result.toll_count

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
    ): View? = inflater.inflate(R.layout.activity_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            searchResult = it.getSerializable(SEARCH_RESULT) as SearchResult
        }
        showResultInfo()
    }

    private fun showResultInfo() {
        group_result.visible()

        origin.text = searchResult.originAddress.toString()
        destination.text = searchResult.destinationAddress.toString()
        distance.text = searchResult.distance.toString()
        distance_unit.text = searchResult.distanceUnit
        duration.text = searchResult.duration.toString()
        duration_unit.text = searchResult.durationUnit
        has_tolls.text = searchResult.hasTolls.toString()
        toll_count.text = searchResult.tollCount.toString()
        toll_cost.text = searchResult.tollCost.toString()
        toll_cost_unit.text = searchResult.tollCostUnit
        provider.text = searchResult.provider
        cached.text = searchResult.cached.toString()
        fuel_usage.text = searchResult.fuelUsage.toString()
        fuel_usage_unit.text = searchResult.fuelUsageUnit
        fuel_cost.text = searchResult.fuelCost.toString()
        fuel_cost_unit.text = searchResult.fuelCostUnit
        refrigerated.text = searchResult.refrigerated.toString()
        general.text = searchResult.general.toString()
        bulk.text = searchResult.bulk.toString()
        neogranel.text = searchResult.neogranel.toString()
        dangerous.text = searchResult.dangerous.toString()
    }
}