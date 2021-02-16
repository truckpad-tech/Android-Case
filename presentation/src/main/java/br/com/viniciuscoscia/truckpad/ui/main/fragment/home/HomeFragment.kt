package br.com.viniciuscoscia.truckpad.ui.main.fragment.home

import android.Manifest
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import br.com.viniciuscoscia.truckpad.R
import br.com.viniciuscoscia.truckpad.common.SearchResult
import br.com.viniciuscoscia.truckpad.common.textNotBlank
import br.com.viniciuscoscia.truckpad.common.textToDouble
import br.com.viniciuscoscia.truckpad.common.textToInt
import br.com.viniciuscoscia.truckpad.entity.PlaceResult
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveDatas()
        setupListeners()
    }

    private fun executeCalc() {
        val origin = viewModel.searchAddresses(etOrigin.text.toString())
        val destiny = viewModel.searchAddresses(etDestiny.text.toString())
        val axis = etAxisNumber.textToInt()

        if (areFieldsValid(axis, origin, destiny)) {
            viewModel.searchRoutesAndPrices(
                etFuelConsumption.textToInt(),
                etFuelPricePerLiter.textToDouble(),
                origin[0],
                destiny[0],
                axis
            )
        } else {
            btnCalculate.hideLoading()
        }
    }

    private fun isDestinyValid(address: List<Address>, textInputLayout: TextInputLayout) =
        if (address.isEmpty()) {
            textInputLayout.error = getString(R.string.insert_a_destiny)
            false
        } else {
            true
        }


    private fun isAxisNumberValid(axis: Int) =
        if (viewModel.validateAxisQuantity(axis).not()) {
            tilAxisNumber.error = getString(R.string.invalid_axis_number_error)
            false
        } else {
            true
        }

    private fun setupLiveDatas() {
        viewModel.originValidator.observe(viewLifecycleOwner, Observer { placeResult ->
            dealWithPlaceResult(placeResult, tilOrigin)
        })

        viewModel.destinyValidator.observe(viewLifecycleOwner, Observer { placeResult ->
            dealWithPlaceResult(placeResult, tilDestiny)
        })

        viewModel.calcResults.observe(viewLifecycleOwner, Observer { repoResult ->
            btnCalculate.hideLoading()
            when (repoResult.searchResult) {
                SearchResult.SUCCESS -> {
                    val action =
                        HomeFragmentDirections.actionNavigationHomeToRouteResultsFragment(
                            repoResult
                                .calcResults!!
                        )
                    requireView().findNavController().navigate(action)
                }
                else -> {
                    showToastMessage(repoResult.message)
                }
            }
        })
    }

    private fun dealWithPlaceResult(placeResult: PlaceResult, textInputLayout: TextInputLayout) {
        when (placeResult.searchResult) {
            SearchResult.SUCCESS -> {
                textInputLayout.isErrorEnabled = false
                textInputLayout.editText?.setText(placeResult.address!!.getAddressLine(0))
            }
            SearchResult.NO_RESULT -> textInputLayout.error = placeResult.errorMessage
            SearchResult.ERROR -> showToastMessage(placeResult.errorMessage)
        }
    }

    private fun showToastMessage(errorMessage: String) {
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun areFieldsValid(
        axis: Int,
        origin: List<Address>,
        destiny: List<Address>
    ): Boolean {
        var areValid = true

        if (isAxisNumberValid(axis).not()) {
            areValid = false
        }
        if (isDestinyValid(origin, tilOrigin).not()) {
            areValid = false
        }
        if (isDestinyValid(destiny, tilDestiny).not()) {
            areValid = false
        }
        if (etFuelConsumption.textNotBlank().not()) {
            tilFuelConsumption.error = getString(R.string.insert_a_value)
            areValid = false
        }
        if (etFuelPricePerLiter.textNotBlank().not()) {
            tilFuelPricePerLiter.error = getString(R.string.insert_a_value)
            areValid = false
        }

        return areValid
    }

    private fun setupListeners() {
        tilOrigin.setEndIconOnClickListener { viewModel.searchForOriginAddress(etOrigin.text.toString()) }
        tilDestiny.setEndIconOnClickListener { viewModel.searchForDestinyAddress(etDestiny.text.toString()) }

        etOrigin.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus.not()) {
                viewModel.searchForOriginAddress(etOrigin.text.toString())
            }
        }

        etDestiny.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus.not()) {
                viewModel.searchForDestinyAddress(etDestiny.text.toString())
            }
        }

        etAxisNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus.not() && etAxisNumber.text.toString().isNotBlank()) {
                isAxisNumberValid(etAxisNumber.text.toString().toInt())
            }
        }

        btnCurrentPlace.setOnClickListener {
            if (viewModel.hasAccessFineLocationPermission()) {
                viewModel.getCurrentLocation()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }

        btnCalculate.setOnClickListener {
            btnCalculate.showLoading()
            executeCalc()
        }

        etOrigin.doOnTextChanged { _, _, _, _ ->
            tilOrigin.isErrorEnabled = false
        }
        etDestiny.doOnTextChanged { _, _, _, _ ->
            tilDestiny.isErrorEnabled = false
        }
        etAxisNumber.doOnTextChanged { _, _, _, _ ->
            tilAxisNumber.isErrorEnabled = false
        }
        etFuelPricePerLiter.doOnTextChanged { _, _, _, _ ->
            tilFuelPricePerLiter.isErrorEnabled = false
        }
        etFuelConsumption.doOnTextChanged { _, _, _, _ ->
            tilFuelConsumption.isErrorEnabled = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return
        }

        if (viewModel.hasAccessFineLocationPermission()) {
            viewModel.getCurrentLocation()
        } else {
            showToastMessage(getString(R.string.need_to_allow_location))
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 81
    }
}
