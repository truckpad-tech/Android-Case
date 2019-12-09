package e.caioluis.android_case.activity.route

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import e.caioluis.android_case.R

class RouteResultActivity : AppCompatActivity(), RouteResultContract.RouteView {

    private lateinit var presenter: RouteResultContract.RoutePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        initVars()
        initActions()
    }

    private fun initVars() {

        presenter = RouteResultPresenter(this)
    }

    private fun initActions() {
        presenter.valuesReceived()
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
