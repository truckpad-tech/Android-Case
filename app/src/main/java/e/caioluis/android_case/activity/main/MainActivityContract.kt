package e.caioluis.android_case.activity.main

interface MainActivityContract {

    interface MainView {

        fun showToastMessage(message: String)
        fun closeKeyboard()
    }

    interface MainPresenter {

        fun handlePermissionResult(result: IntArray)
        fun bottomSheetClicked()
        fun startApp()
        fun handleStartPointClick()
        fun handleSearchClick()
        fun handleDestinationClick()
        fun handleSetLocationClick()
        fun handleCalculateClick()
        fun handleSeeHistoryClick()
    }
}