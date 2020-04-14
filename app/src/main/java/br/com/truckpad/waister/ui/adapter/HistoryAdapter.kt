package br.com.truckpad.waister.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.truckpad.waister.R
import br.com.truckpad.waister.domain.History
import br.com.truckpad.waister.ui.activity.ResultActivity
import br.com.truckpad.waister.util.formatDistance
import com.google.gson.Gson

class HistoryAdapter(private val activity: Activity) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var dataArray: ArrayList<History> = ArrayList()

    fun setData(data: ArrayList<History>) {
        this.dataArray = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.item_phone, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataArray.size > position) {
            holder.setData(dataArray[position])
        }
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private var tvSubtitle = itemView.findViewById<TextView>(R.id.tv_subtitle)

        fun setData(history: History) {
            val title = "${history.origin} -> ${history.destination}"
            val calculate = history.calculate

            tvTitle.text = title
            tvSubtitle.text = calculate.distance.formatDistance(calculate.distance_unit)

            itemView.setOnClickListener {
                val intent = Intent(activity, ResultActivity::class.java)
                intent.putExtra(ResultActivity.PARAM_EXTRA_ORIGIN, history.origin)
                intent.putExtra(ResultActivity.PARAM_EXTRA_DESTINATION, history.destination)
                intent.putExtra(ResultActivity.PARAM_EXTRA_AXIS, history.axis)
                intent.putExtra(
                    ResultActivity.PARAM_EXTRA_CALCULATE,
                    Gson().toJson(calculate).toString()
                )
                intent.putExtra(
                    ResultActivity.PARAM_EXTRA_ANTT_PRICE,
                    Gson().toJson(history.anttPrice).toString()
                )
                activity.startActivity(intent)
            }
        }
    }

}