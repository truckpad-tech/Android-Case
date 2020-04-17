package dev.khalil.freightpad.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dev.khalil.freightpad.R
import dev.khalil.freightpad.common.BRAZIL_CURRENCY_SYMBOL
import dev.khalil.freightpad.common.END_LOCATION_INTENT
import dev.khalil.freightpad.common.ORIGIN_LOCATION_INTENT
import dev.khalil.freightpad.databinding.FragmentInfoBinding
import dev.khalil.freightpad.di.infoModule
import dev.khalil.freightpad.extensions.viewModel
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

  private fun initViewModel() {
  }

  private fun initObservers() {
    infoViewModel.axis.observe(viewLifecycleOwner, Observer { binding.axisDisplay.text = "$it" })
  }

  private fun initListeners() {
    binding.startLocation.setOnClickListener { openSearchActivity(ORIGIN_LOCATION_INTENT) }
    binding.endLocation.setOnClickListener { openSearchActivity(END_LOCATION_INTENT) }
    binding.axisDecrement.setOnClickListener { infoViewModel.decrementAxis() }
    binding.axisIncrement.setOnClickListener { infoViewModel.incrementAxis() }
    binding.fuelConsume.addTextChangedListener(DecimalNumberFormatter(binding.fuelConsume))
    binding.fuelPrice.addTextChangedListener(
      DecimalNumberFormatter(
        binding.fuelPrice,
        BRAZIL_CURRENCY_SYMBOL
      )
    )
  }

  private fun openSearchActivity(requestCode: Int) {
    activity?.run {
      startActivityForResult(SearchActivity.createIntent(this), requestCode)
    }
  }

}