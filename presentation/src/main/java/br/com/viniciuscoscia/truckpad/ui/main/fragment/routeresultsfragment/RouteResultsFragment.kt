package br.com.viniciuscoscia.truckpad.ui.main.fragment.routeresultsfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.viniciuscoscia.truckpad.R
import kotlinx.android.synthetic.main.fragment_route_results.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RouteResultsFragment : Fragment() {

    private val viewModel: RouteResultsViewModel by viewModel()
    private val routeResultsArgs by navArgs<RouteResultsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_route_results, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(routeResultsArgs.CalcResults) {
            tvOrigin.text = placeOfOriginName
            tvDestiny.text = placeOfDestinyName
            tvAxis.text = axis.toString()
            tvDistance.text = "$distance $distanceUnit"
            tvDuration.text = "$duration $durationUnit"
            tvToll.text = "$tollCostUnit $tollCost"
            tvNecessaryFuel.text = "$fuelConsumption $necessaryFuelUnity"
            tvTotalFuelCost.text = "$totalFuelCostUnit $totalFuelCost"
            tvTotal.text = "R$ $totalCost"
            tvGeneral.text = "R$ $geral"
            tvBulk.text = "R$ $granel"
            tvNeobulk.text = "R$ $neogranel"
            tvFridged.text = "R$ $frigorificada"
            tvDangerous.text = "R$ $perigosa"
        }
    }
}
