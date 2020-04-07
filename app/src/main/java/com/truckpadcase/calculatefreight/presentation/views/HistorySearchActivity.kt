package com.truckpadcase.calculatefreight.presentation.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.truckpadcase.calculatefreight.R
import com.truckpadcase.calculatefreight.presentation.adapters.HistoryItemAdapter
import com.truckpadcase.calculatefreight.presentation.viewmodels.HistorySearchViewModelImpl
import com.truckpadcase.calculatefreight.presentation.views.base.BaseActivity
import kotlinx.android.synthetic.main.activity_history_search.*

class HistorySearchActivity : BaseActivity() {

    val viewModelImpl: HistorySearchViewModelImpl by lazy {
        ViewModelProviders.of(this).get(HistorySearchViewModelImpl::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_search)

        history_progress_bar.visibility = View.VISIBLE

        setupToolbar(toolbar_history_screen, R.string.historic_title,true )

        val recyclerView = recycle_view
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        viewModelImpl.getHistory().observe(this, Observer {
            recycle_view.adapter = HistoryItemAdapter(it, this)
            history_progress_bar.visibility = View.INVISIBLE
        })

    }

    companion object {

        fun getStartIntent(context: Context) : Intent {
            return Intent(context, HistorySearchActivity::class.java).apply {}
        }
    }
}
