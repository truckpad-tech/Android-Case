package e.caioluis.android_case.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import e.caioluis.android_case.R
import e.caioluis.android_case.model.RouteResult
import e.caioluis.android_case.util.convertMetersToKm
import e.caioluis.android_case.util.intToString
import kotlinx.android.synthetic.main.history_list_item.view.*

class HistoryAdapter(
    private val routes: List<RouteResult>,
    private val onItemClickListener: ((routeResult: RouteResult) -> Unit)
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)

        return HistoryViewHolder(view, onItemClickListener)
    }

    override fun getItemCount(): Int = routes.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindView(routes[position])
    }

    class HistoryViewHolder(
        itemView: View,
        val onItemClickListener: ((routeResult: RouteResult) -> Unit)
    ) : RecyclerView.ViewHolder(itemView) {

        private val address1 = itemView.item_initial_address
        private val address2 = itemView.item_final_address
        private val routeNumber = itemView.item_number

        fun bindView(routeResult: RouteResult) {

            routeNumber.text = routeResult.distance.convertMetersToKm()
            address1.text = routeResult.initial_address
            address2.text = routeResult.final_address

            itemView.setOnClickListener {

                onItemClickListener(routeResult)
            }
        }
    }
}