package e.caioluis.android_case.activity.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import e.caioluis.android_case.R
import e.caioluis.android_case.activity.route.RouteResultPresenter
import e.caioluis.android_case.adapter.HistoryAdapter
import e.caioluis.android_case.model.RouteResult
import e.caioluis.android_case.util.Constants
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity(), HistoryActivityContract.HistoryView {

    private lateinit var mContext: Context
    private lateinit var presenter: HistoryActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        initVars()
        initActions()
    }

    private fun initVars() {

        mContext = this@HistoryActivity
        presenter = HistoryActivityPresenter(this)
    }

    private fun initActions() {

        setSupportActionBar(history_toolbar)
        presenter.activityStarted()
    }

    companion object {

        fun getHistoryStartIntent(context: Context): Intent {

            return Intent(context, HistoryActivity::class.java)
        }
    }

    override fun showList(routeList: List<RouteResult>) {

        history_recyclerView.layoutManager = LinearLayoutManager(
            mContext,
            RecyclerView.VERTICAL,
            false
        )

        history_recyclerView.adapter = HistoryAdapter(routeList) { route ->

            val intent = RouteResultPresenter.getRouteStartIntent(
                mContext,
                route,
                Constants.HISTORY_ACTIVITY
            )

            mContext.startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuItem_delete_history -> {

                presenter.handleDeleteHistoryClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showToastMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}