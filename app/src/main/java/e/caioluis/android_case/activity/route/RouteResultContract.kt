package e.caioluis.android_case.activity.route

interface RouteResultContract {

    interface RouteView {
        fun showToastMessage(message: String)
    }

    interface RoutePresenter {

        fun valuesReceived()
        fun callHistoryActivity()
    }
}