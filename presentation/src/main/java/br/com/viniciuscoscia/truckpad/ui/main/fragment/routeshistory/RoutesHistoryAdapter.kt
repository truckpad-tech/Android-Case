package br.com.viniciuscoscia.truckpad.ui.main.fragment.routeshistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.viniciuscoscia.truckpad.R
import br.com.viniciuscoscia.truckpad.common.toBrazilianDateTimeFormat
import br.com.viniciuscoscia.truckpad.domain.entities.CalcResults
import java.util.*

class RoutesHistoryAdapter(val onClickListener: (CalcResults) -> Unit) :
    RecyclerView.Adapter<RoutesHistoryAdapter.ViewHolder>() {

    var calcResults: List<CalcResults> = arrayListOf()
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.calc_result_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = calcResults.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(calcResults[position])
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        private val origin: TextView = itemView.findViewById(R.id.tvOrigin)
        private val destiny: TextView = itemView.findViewById(R.id.tvDestiny)
        private val dateTime: TextView = itemView.findViewById(R.id.tvDateTime)

        fun bind(calcResults: CalcResults) {
            origin.text = calcResults.placeOfOriginName
            destiny.text = calcResults.placeOfDestinyName
            dateTime.text = Date(calcResults.timeStamp).toBrazilianDateTimeFormat()
        }

        override fun onClick(v: View) {
            run { onClickListener(calcResults[adapterPosition]) }
        }
    }
}