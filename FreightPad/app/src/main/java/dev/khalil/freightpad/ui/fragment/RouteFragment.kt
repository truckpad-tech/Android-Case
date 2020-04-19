package dev.khalil.freightpad.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dev.khalil.freightpad.R
import dev.khalil.freightpad.databinding.FragmentRouteBinding
import dev.khalil.freightpad.model.RouteUiModel
import dev.khalil.freightpad.model.UiState.ROUTE
import dev.khalil.freightpad.ui.activity.OnShowRoute

class RouteFragment : Fragment(), RouteInfo {

  private lateinit var binding: FragmentRouteBinding

  private var route: RouteUiModel? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_route, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initView()
  }

  private fun initView() {
    binding.routeInfoNavMap.setOnClickListener {
      route?.let {
        activity?.run {
          val calculatorFragment = this.supportFragmentManager.fragments.first { fragment ->
            fragment is CalculatorFragment
          } as CalculatorFragment

          calculatorFragment.collapseBottomSheet()
        }
      }
    }
  }

  override fun showRoute(route: RouteUiModel) {
    this.route = route
    showRouteInfo()
    setRouteFragmentVisible()
  }

  private fun setRouteFragmentVisible() {
    activity?.run {
      val viewPagerControl = this.supportFragmentManager.fragments.first { fragment ->
        fragment is CalculatorFragment
      } as ViewPagerControl

      viewPagerControl.setPage(ROUTE)

      route?.let {
        val onShowRoute = this as OnShowRoute
        onShowRoute.showRoute(it.route.first())
      }
    }
  }

  private fun showRouteInfo() {
    route?.let { route ->
      with(binding) {
        routeInfoStartLocation.text = route.points.first().displayName
        routeInfoDestinationLocation.text = route.points.last().displayName
        routeInfoDistance.text = route.distance
        routeInfoDuration.text = route.duration
        routeTollCount.text = route.tollCount
        routeTollCost.text = route.tollCost
        routeFuelNeeded.text = route.fuelNeeded
        routeFuelTotalCost.text = route.fuelTotalCost
        routeFuelTollTotal.text = route.totalCost
        routeAnttGeneral.text = route.general
        routeAnttBulk.text = route.bulk
        routeAnttNeobulk.text = route.neoBulk
        routeAnttRefrigerated.text = route.refrigerated
        routeAnttDangerous.text = route.dangerous
      }
    }
  }

}