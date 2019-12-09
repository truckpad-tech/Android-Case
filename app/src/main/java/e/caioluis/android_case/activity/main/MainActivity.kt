package e.caioluis.android_case.activity.main

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import e.caioluis.android_case.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    MainActivityContract.MainView, View.OnClickListener {

    private lateinit var presenter: MainActivityContract.MainPresenter
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initVars()
        initActions()
    }

    private fun initVars() {

        presenter = MainActivityPresenter(this)
        builder = AlertDialog.Builder(this)
    }

    private fun initActions() {
        setListeners()
        presenter.startApp()
    }

    override fun onClick(view: View) {

        when (view.id) {

            R.id.main_bottom_sheet -> {
                presenter.bottomSheetClicked()
            }

            R.id.bottom_btn_calculate -> {
                presenter.handleCalculateClick()
            }

            R.id.map_btn_search -> {
                presenter.handleSearchClick()
            }

            R.id.bottom_tv_startPoint -> {
                presenter.handleStartPointClick()
            }

            R.id.bottom_tv_destination -> {
                presenter.handleDestinationClick()
            }

            R.id.map_btn_setLocation -> {
                presenter.handleSetLocationClick()
            }

            R.id.bottom_btn_history -> {
                presenter.handleSeeHistoryClick()
            }
        }
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        presenter.handlePermissionResult(grantResults)
    }

    private fun setListeners() {

        main_bottom_sheet.setOnClickListener(this)
        bottom_btn_calculate.setOnClickListener(this)
        map_btn_search.setOnClickListener(this)
        bottom_tv_startPoint.setOnClickListener(this)
        bottom_tv_destination.setOnClickListener(this)
        map_btn_setLocation.setOnClickListener(this)
        bottom_btn_history.setOnClickListener(this)
    }

    private fun showExitAlert() {

        with(builder) {

            setMessage(context.getString(R.string.exit_alert_message))
            setPositiveButton(getString(R.string.label_cancel)) { _, _ ->

            }

            setNegativeButton(R.string.label_exit) { _, _ ->
                finish()
            }

            show()
        }
    }

    override fun onBackPressed() {
        showExitAlert()
    }

    override fun closeKeyboard() {
        val view: View? = this.currentFocus

        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
