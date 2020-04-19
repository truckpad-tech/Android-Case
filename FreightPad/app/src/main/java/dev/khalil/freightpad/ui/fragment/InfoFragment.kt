package dev.khalil.freightpad.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dev.khalil.freightpad.R
import dev.khalil.freightpad.common.ADDRESS_KEY
import dev.khalil.freightpad.common.BRAZIL_CURRENCY_SYMBOL
import dev.khalil.freightpad.common.DESTINATION_LOCATION_CODE
import dev.khalil.freightpad.common.LAT_KEY
import dev.khalil.freightpad.common.LONG_KEY
import dev.khalil.freightpad.common.START_LOCATION_CODE
import dev.khalil.freightpad.databinding.FragmentInfoBinding
import dev.khalil.freightpad.di.infoModule
import dev.khalil.freightpad.extensions.gone
import dev.khalil.freightpad.extensions.removeMeasureUnit
import dev.khalil.freightpad.extensions.viewModel
import dev.khalil.freightpad.extensions.visible
import dev.khalil.freightpad.model.Place
import dev.khalil.freightpad.ui.activity.SearchActivity
import dev.khalil.freightpad.ui.viewModel.InfoFragmentViewModel
import dev.khalil.freightpad.utils.DecimalNumberFormatter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class InfoFragment : Fragment(), KodeinAware {

  override val kodein = Kodein.lazy {
    import(infoModule)
  }

  private lateinit var binding: FragmentInfoBinding

  private val infoViewModel: InfoFragmentViewModel by viewModel()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initViewModel()
    initListeners()
    initObservers()
  }

  override fun onActivityResult(fromCode: Int, resultCode: Int, data: Intent?) {
    when (resultCode) {
      RESULT_OK -> {
        data?.let {
          val name = it.getStringExtra(ADDRESS_KEY)
          val lat = it.getDoubleExtra(LAT_KEY, 0.0)
          val long = it.getDoubleExtra(LONG_KEY, 0.0)

          if (!name.isNullOrEmpty()) {
            infoViewModel.setLocation(Place(name, listOf(long, lat)), fromCode)
          }
        }
      }
    }
  }

  override fun onDestroy() {
    infoViewModel.onDestroy()
    super.onDestroy()
  }

  private fun initViewModel() {
  }

  private fun initObservers() {
    infoViewModel.axis.observe(viewLifecycleOwner, Observer { binding.axisDisplay.text = "$it" })
    infoViewModel.start.observe(viewLifecycleOwner,
      Observer { binding.startLocation.text = it.displayName })
    infoViewModel.destination.observe(viewLifecycleOwner,
      Observer { binding.destinationLocation.text = it.displayName })
    infoViewModel.route.observe(viewLifecycleOwner, Observer {
      activity?.run {
        val routeInfo = this.supportFragmentManager.fragments.first { fragment ->
          fragment is CalculatorFragment
        }
          .childFragmentManager.fragments.first() { childFragment ->
            childFragment is RouteFragment
          } as RouteInfo
        routeInfo.showRoute(it)
      }
    })
    infoViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
      if (isLoading) {
        binding.calculate.isClickable = false
        binding.progressBar.visible()
        binding.loadingAnimation.playAnimation()
      } else {
        binding.calculate.isClickable = true
        binding.progressBar.gone()
        binding.loadingAnimation.pauseAnimation()
      }
    })
    infoViewModel.error.observe(viewLifecycleOwner, Observer { stringId ->
      if (stringId == R.string.error_description) {
        binding.calculate.setText(R.string.error_try_again)
      } else {
        binding.calculate.setText(R.string.f_info_calculate)
      }

      binding.errorDescription.setText(getString(stringId))
    })
  }

  private fun initListeners() {
    binding.startLocation.setOnClickListener { openSearchActivity(START_LOCATION_CODE) }
    binding.destinationLocation.setOnClickListener { openSearchActivity(DESTINATION_LOCATION_CODE) }
    binding.axisDecrement.setOnClickListener { infoViewModel.decrementAxis() }
    binding.axisIncrement.setOnClickListener { infoViewModel.incrementAxis() }
    binding.fuelConsume.addTextChangedListener(DecimalNumberFormatter(binding.fuelConsume))
    binding.fuelPrice.addTextChangedListener(
      DecimalNumberFormatter(
        binding.fuelPrice,
        BRAZIL_CURRENCY_SYMBOL
      )
    )
    binding.calculate.setOnClickListener {
      val fuelConsume = binding.fuelConsume.text.removeMeasureUnit()
      val fuelPrice = binding.fuelPrice.text.removeMeasureUnit()
      infoViewModel.calculate(fuelConsume, fuelPrice)
    }
  }

  private fun openSearchActivity(intentCode: Int) {
    context?.run {
      startActivityForResult(SearchActivity.createIntent(this), intentCode)
    }
  }
}