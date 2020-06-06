package com.jonas.truckpadchallenge.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.core.utils.gone
import com.jonas.truckpadchallenge.core.utils.visible
import com.jonas.truckpadchallenge.search.domain.entities.SearchResult
import kotlinx.android.synthetic.main.fragment_result.bulk
import kotlinx.android.synthetic.main.fragment_result.cached
import kotlinx.android.synthetic.main.fragment_result.dangerous
import kotlinx.android.synthetic.main.fragment_result.distance
import kotlinx.android.synthetic.main.fragment_result.distance_unit
import kotlinx.android.synthetic.main.fragment_result.duration
import kotlinx.android.synthetic.main.fragment_result.duration_unit
import kotlinx.android.synthetic.main.fragment_result.fuel_cost
import kotlinx.android.synthetic.main.fragment_result.fuel_cost_unit
import kotlinx.android.synthetic.main.fragment_result.fuel_usage
import kotlinx.android.synthetic.main.fragment_result.fuel_usage_unit
import kotlinx.android.synthetic.main.fragment_result.general
import kotlinx.android.synthetic.main.fragment_result.group_result
import kotlinx.android.synthetic.main.fragment_result.has_tolls
import kotlinx.android.synthetic.main.fragment_result.neogranel
import kotlinx.android.synthetic.main.fragment_result.provider
import kotlinx.android.synthetic.main.fragment_result.refrigerated
import kotlinx.android.synthetic.main.fragment_result.toll_cost
import kotlinx.android.synthetic.main.fragment_result.toll_cost_unit
import kotlinx.android.synthetic.main.fragment_result.toll_count

class ResultFragment : Fragment() {

    lateinit var searchResult: SearchResult

    companion object {
        const val PARAM = "SearchResult"

        fun newInstance(searchResult: SearchResult? = null) =
            ResultFragment().apply {
                arguments = Bundle().apply { putSerializable(PARAM, searchResult) }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkCurrentState()
    }

    private fun checkCurrentState() {
        arguments?.let {
            val bundle = it.getSerializable(PARAM)
            if (bundle != null) searchResult = bundle as SearchResult
        }

        if (::searchResult.isInitialized) showResultInfo() else showEmptyInfo()
    }

    private fun showResultInfo() {
        group_result.visible()

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

    private fun showEmptyInfo() {
        group_result.gone()
    }
}