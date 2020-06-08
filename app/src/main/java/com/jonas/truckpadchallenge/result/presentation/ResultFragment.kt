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
import kotlinx.android.synthetic.main.activity_result.bulk
import kotlinx.android.synthetic.main.activity_result.dangerous
import kotlinx.android.synthetic.main.activity_result.destination
import kotlinx.android.synthetic.main.activity_result.distance
import kotlinx.android.synthetic.main.activity_result.duration
import kotlinx.android.synthetic.main.activity_result.fuel_cost
import kotlinx.android.synthetic.main.activity_result.fuel_usage
import kotlinx.android.synthetic.main.activity_result.general
import kotlinx.android.synthetic.main.activity_result.group_toll
import kotlinx.android.synthetic.main.activity_result.has_tolls
import kotlinx.android.synthetic.main.activity_result.neogranel
import kotlinx.android.synthetic.main.activity_result.origin
import kotlinx.android.synthetic.main.activity_result.refrigerated
import kotlinx.android.synthetic.main.activity_result.toll_cost
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
        origin.text = searchResult.originAddress.toString()
        destination.text = searchResult.destinationAddress.toString()
        distance.text = searchResult.distance.toString().plus(searchResult.distanceUnit)
        duration.text = searchResult.duration.toString().plus(searchResult.durationUnit)
        fuel_usage.text = searchResult.fuelUsage.toString().plus(searchResult.fuelUsageUnit)
        fuel_cost.text = searchResult.fuelCostUnit.plus(searchResult.fuelCost.toString())
        refrigerated.text =
            searchResult.refrigerated.toString().plus(getString(R.string.result_tolls))
        general.text = searchResult.general.toString().plus(getString(R.string.result_tolls))
        bulk.text = searchResult.bulk.toString().plus(getString(R.string.result_tolls))
        neogranel.text = searchResult.neogranel.toString().plus(getString(R.string.result_tolls))
        dangerous.text = searchResult.dangerous.toString().plus(getString(R.string.result_tolls))

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