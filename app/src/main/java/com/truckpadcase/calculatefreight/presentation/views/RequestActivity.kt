package com.truckpadcase.calculatefreight.presentation.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.truckpadcase.calculatefreight.R
import com.truckpadcase.calculatefreight.databinding.ActivityRequestBinding
import com.truckpadcase.calculatefreight.presentation.viewmodels.RequestViewModelImpl
import com.truckpadcase.calculatefreight.presentation.views.base.BaseActivity
import com.truckpadcase.calculatefreight.utils.Constants.hideKeyboard
import kotlinx.android.synthetic.main.activity_request.*


open class RequestActivity :  BaseActivity() {

    lateinit var mainBinding : ActivityRequestBinding

    val viewModelImpl: RequestViewModelImpl by lazy {
        ViewModelProviders.of(this).get(RequestViewModelImpl::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_request)

        mainBinding.lifecycleOwner = this

        mainBinding.viewmodel = viewModelImpl

        setupToolbar(toolbar_search_route, R.string.app_name, true)

        viewModelImpl.foundMyLocation()

        destination_city_edt_txt.requestFocus()

        identifyError()

        loadingObserve()
    }

    private fun identifyError() {
        viewModelImpl.showError.observe(this, Observer { errorMsg ->
            form_layout.visibility = View.VISIBLE
            indeterminateBar.visibility = View.INVISIBLE
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
        })
    }

    private fun loadingObserve() {
        viewModelImpl.showProgress.observe(this, Observer { showProgresBar ->
            if (!showProgresBar){
                finish()
            }
            hideKeyboard()
            form_layout.visibility = View.INVISIBLE
            indeterminateBar.visibility = View.VISIBLE
        })
    }

    companion object {

        fun getStartIntent(context: Context) : Intent {
            return Intent(context, RequestActivity::class.java).apply {}
        }
    }

}
