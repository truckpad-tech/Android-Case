package e.caioluis.android_case.activity.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import e.caioluis.android_case.R
import kotlinx.android.synthetic.main.activity_maps.*

class MainActivity : AppCompatActivity(),
    MainActivityContract.MainView, View.OnClickListener {

    private lateinit var bSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheet: View
    private lateinit var presenter: MainActivityContract.MainPresenter
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        initVars()
        initActions()
    }

    private fun initVars() {

        bottomSheet = findViewById<View>(R.id.frag_bottom_sheet)
        bSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        presenter = MainActivityPresenter(this)

        builder = AlertDialog.Builder(this)
    }

    private fun initActions() {
        setListeners()
        presenter.startApp()
    }

    override fun onClick(view: View) {

        when (view.id) {

            R.id.frag_bottom_sheet -> {

                presenter.bottomSheetClicked()
            }

            R.id.bottom_btn_calculate -> {

            }

            R.id.map_btn_search -> {
                presenter.handleSearch()
            }

            R.id.bottom_tv_startPoint -> {

                presenter.handleStartPointClick()
            }

            R.id.bottom_tv_finalPoint -> {

                presenter.handleFinalPointClick()
            }

            R.id.map_btn_useLocation -> {

                presenter.handleUseLocationClick()
            }
        }
    }

    override fun expandBottomSheet(boolean: Boolean) {

        if (boolean) {
            bSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showUseLocationButton(boolean: Boolean) {
        map_btn_useLocation.isVisible = boolean
    }

    override fun showProgressBar(boolean: Boolean) {
        map_progressBar.isVisible = boolean
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        presenter.handlePermissionResult(grantResults)
    }

    private fun setListeners() {

        frag_bottom_sheet.setOnClickListener(this)
        bottom_btn_calculate.setOnClickListener(this)
        map_btn_search.setOnClickListener(this)
        bottom_tv_startPoint.setOnClickListener(this)
        bottom_tv_finalPoint.setOnClickListener(this)
        map_btn_useLocation.setOnClickListener(this)
    }

    override fun onBackPressed() {
        showExitAlert()
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
}
