package e.caioluis.android_case.activity.main

interface MainActivityContract {

    interface MainView {
        fun showProgressBar(boolean: Boolean)
        fun expandBottomSheet(boolean: Boolean)
        fun showToastMessage(message: String)
        fun showUseLocationButton(boolean: Boolean)
    }

    interface MainPresenter {

        fun handlePermissionResult(result: IntArray)
        fun bottomSheetClicked()
        fun startApp()
        fun handleStartPointClick()
        fun handleSearch()
        fun handleFinalPointClick()
        fun handleUseLocationClick()
    }
}