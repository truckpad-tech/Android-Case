package e.caioluis.android_case.activity.route

import android.app.Activity
import android.content.Context
import android.content.Intent
import e.caioluis.android_case.activity.history.HistoryActivity
import e.caioluis.android_case.database.HistoryDatabase
import e.caioluis.android_case.model.RouteResult
import e.caioluis.android_case.util.*
import kotlinx.android.synthetic.main.activity_route.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class RouteResultPresenter(
    private val routeView: RouteResultContract.RouteView

) : RouteResultContract.RoutePresenter {

    private val routeActivity = routeView as Activity
    private lateinit var routeResult: RouteResult

    override fun valuesReceived() {

        routeResult = routeActivity.intent.getSerializableExtra(ROUTE_RESULT) as RouteResult
        val whoIsCalling = routeActivity.intent.getIntExtra(WHO_IS_CALLING, -1)

        if (whoIsCalling == Constants.MAIN_ACTIVITY) {

            CoroutineScope(IO).launch {

                saveRouteOnDB()
            }
        }

        setTextValues(routeResult)
    }

    override fun callHistoryActivity() {
        val intent = HistoryActivity.getHistoryStartIntent(routeActivity)
        routeActivity.startActivity(intent)
        routeActivity.finish()
    }

    private suspend fun saveRouteOnDB() {

        try {
            HistoryDatabase.getInstance(routeActivity)
                .historyDao()
                .insert(routeResult)

        } catch (ex: Exception) {
            routeView.showToastMessage(ex.message.toString())
        }
    }

    private fun setTextValues(routeResult: RouteResult) {
        with(routeActivity) {
            route_tv_distance.text = routeResult.distance.convertMetersToKm()
            route_tv_duration.text = routeResult.duration.secondsToHours()

            route_tv_consumeLiters.text = routeResult.fuel_usage.formatLiters()
            route_tv_consumePrice.text = routeResult.fuel_cost.toBRLCurrency()
            route_tv_tollsNumber.text = routeResult.toll_count.intToString()
            route_tv_tollsPrice.text = routeResult.toll_cost.toDouble().toBRLCurrency()

            route_tv_totalCost.text = routeResult.total_cost.toBRLCurrency()

            route_tv_refrigerated.text = routeResult.refrigerated.toBRLCurrency()
            route_tv_general.text = routeResult.general.toBRLCurrency()
            route_tv_granel.text = routeResult.granel.toBRLCurrency()
            route_tv_neogranel.text = routeResult.neogranel.toBRLCurrency()
            route_tv_hazardous.text = routeResult.hazardous.toBRLCurrency()
        }
    }

    companion object {

        private const val ROUTE_RESULT = "ROUTE_RESULT"
        private const val WHO_IS_CALLING = "WHO_IS_CALLING"

        fun getRouteStartIntent(
            context: Context,
            routeResult: RouteResult,
            whoIsCalling: Int
        ): Intent {

            return Intent(context, RouteResultActivity::class.java).apply {

                putExtra(ROUTE_RESULT, routeResult)
                putExtra(WHO_IS_CALLING, whoIsCalling)
            }
        }
    }
}