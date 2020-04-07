package com.truckpadcase.calculatefreight.presentation.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.truckpadcase.calculatefreight.R
import com.truckpadcase.calculatefreight.domain.model.local.FreightData
import com.truckpadcase.calculatefreight.presentation.views.ResultSearchActivity
import com.truckpadcase.calculatefreight.utils.Constants.SEARCH_RESULT_ID
import kotlinx.android.synthetic.main.route_history_item.view.*

class HistoryItemAdapter(private val history_search: List<FreightData>, private val context : Context) : RecyclerView.Adapter<HistoryItemAdapter.HistoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.route_history_item, parent, false)
        return HistoryHolder(view)
    }

    override fun getItemCount(): Int {
       return history_search.size
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {

        val note = history_search[position]
        holder?.let {
            it.bindView(note, context)
        }

    }

    class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

        fun bindView(freightData: FreightData, context: Context)  {
            val origin = itemView.origin_text_card_view
            val destiny = itemView.destination_text_card_view

            origin.text = freightData.initial_address
            destiny.text = freightData.final_address

            itemView.setOnClickListener( View.OnClickListener {
                view ->
                val intent = ResultSearchActivity.getStartIntent( context )
                intent.putExtra(SEARCH_RESULT_ID, freightData.uid.toLong() )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)

            })
        }

    }
}