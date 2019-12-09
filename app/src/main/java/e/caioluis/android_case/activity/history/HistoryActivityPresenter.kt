package e.caioluis.android_case.activity.history

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import e.caioluis.android_case.database.HistoryDatabase
import e.caioluis.android_case.model.RouteResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivityPresenter(
    private val historyView: HistoryActivityContract.HistoryView
) : HistoryActivityContract.HistoryPresenter {

    private val historyActivity = historyView as Activity
    private lateinit var routeList: List<RouteResult>
    private var builder = AlertDialog.Builder(historyActivity)

    override fun activityStarted() {

        CoroutineScope(IO).launch {

            withContext(Main) {
                getList()
            }

            withContext(Main) {
                showList(routeList)
            }
        }
    }

    override fun handleDeleteHistoryClick() {
        showDeleteAlert()
    }

    private fun deleteConfirmed() {

        CoroutineScope(IO).launch {

            deleteAllFromDB()
        }

        historyActivity.finish()
    }

    private suspend fun deleteAllFromDB() {

        try {
            HistoryDatabase.getInstance(historyActivity).historyDao().deleteAllRoutes()

        } catch (ex: java.lang.Exception) {

            historyView.showToastMessage(ex.message.toString())
        }
    }

    private fun showList(routeList: List<RouteResult>) {

        historyView.showList(routeList)
    }

    private suspend fun getList() {

        try {
            routeList = HistoryDatabase.getInstance(historyActivity).historyDao().getAllRoutes()

        } catch (ex: Exception) {
            historyView.showToastMessage(ex.message.toString())
        }
    }

    private fun showDeleteAlert() {

        with(builder) {

            setTitle(e.caioluis.android_case.R.string.alert_title_warning)
            setMessage(historyActivity.getString(e.caioluis.android_case.R.string.alert_delete_all_history_confirmation))
            setPositiveButton(historyActivity.getString(e.caioluis.android_case.R.string.label_cancel)) { _, _ ->

            }

            setNegativeButton(historyActivity.getString(e.caioluis.android_case.R.string.label_yes)) { _, _ ->

                deleteConfirmed()
            }

            show()
        }
    }
}