package com.diegobezerra.truckpadcase.ui.main.calculator.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.diegobezerra.truckpadcase.R
import com.diegobezerra.truckpadcase.databinding.FragmentSearchBinding
import com.diegobezerra.truckpadcase.domain.model.Place
import com.diegobezerra.truckpadcase.ui.main.MainViewModel
import com.diegobezerra.truckpadcase.util.setAdapterAndShowDropdownIfNotEmpty
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mainViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            // Origin
            originTextView.setOnFocusChangeListener { v, hasFocus ->
                useLocationButton.isGone = !hasFocus
            }
            originTextView.setOnItemClickListener { v, _, position, _ ->
                mainViewModel.onChooseOrigin(position)
            }
            originTextView.addTextChangedListener(
                afterTextChanged = {
                    val newText = it.toString()
                    // Prevent triggering autocomplete if text is current location
                    if (newText != getString(R.string.text_current_location)) {
                        mainViewModel.onChangedOrigin(it.toString())
                    }
                }
            )

            // Destination
            destinationTextView.setOnItemClickListener { v, _, position, _ ->
                mainViewModel.onChooseDestination(position)
            }
            destinationTextView.addTextChangedListener(
                afterTextChanged = {
                    mainViewModel.onChangedDestination(it.toString())
                }
            )

            // Others
            axisPicker.setOnNumberChangeListener {
                mainViewModel.onChangeAxis(it)
            }

            fuelConsumptionEditText.setOnDecimalChangeListener {
                mainViewModel.onChangeFuelConsumption(it.toFloat())
            }

            fuelPriceEditText.setOnDecimalChangeListener {
                mainViewModel.onChangeFuelPrice(it.toFloat())
            }
        }


        mainViewModel.searchUiModel.observe(viewLifecycleOwner, Observer {
            it.origin ?: return@Observer

            if (it.origin!!.displayName == getString(R.string.text_current_location)) {
                binding.originTextView.setText(it.origin!!.displayName)
                binding.destinationTextView.requestFocus()
            }
        })

        mainViewModel.autocompleteOrigins.observe(viewLifecycleOwner, Observer { places ->
            buildAutocompleteAdapter(places).let {
                binding.originTextView.setAdapterAndShowDropdownIfNotEmpty(it)
            }
        })

        mainViewModel.autocompleteDestinations.observe(viewLifecycleOwner, Observer { places ->
            buildAutocompleteAdapter(places).let {
                binding.destinationTextView.setAdapterAndShowDropdownIfNotEmpty(it)
            }
        })
    }

    private fun buildAutocompleteAdapter(items: List<Place>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items.map { it.displayName })
    }

}
