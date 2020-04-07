package com.truckpadcase.calculatefreight.presentation.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.truckpadcase.calculatefreight.R
import com.truckpadcase.calculatefreight.presentation.adapters.HistoryItemAdapter
import com.truckpadcase.calculatefreight.presentation.viewmodels.HistorySearchViewModel
import com.truckpadcase.calculatefreight.presentation.views.base.BaseActivity
import kotlinx.android.synthetic.main.activity_history_search.*

class HistorySearchActivity : BaseActivity() {

    val viewModel: HistorySearchViewModel by lazy {
        ViewModelProviders.of(this).get(HistorySearchViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_search)

        setupToolbar(toolbar_history_screen, R.string.historic_title,true )

        val recyclerView = recycle_view
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        viewModel.getHistory().observe(this, Observer {
            recycle_view.adapter = HistoryItemAdapter(it, this)
        })

    }

    companion object {

        fun getStartIntent(context: Context) : Intent {
            return Intent(context, HistorySearchActivity::class.java).apply {}
        }
    }
}
