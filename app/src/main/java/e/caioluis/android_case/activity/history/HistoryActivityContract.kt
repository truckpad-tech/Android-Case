package e.caioluis.android_case.activity.history

import e.caioluis.android_case.model.RouteResult

interface HistoryActivityContract {

    interface HistoryView {

        fun showList(routeList: List<RouteResult>)
        fun showToastMessage(message: String)
    }

    interface HistoryPresenter {

        fun activityStarted()
        fun handleDeleteHistoryClick()
    }
}