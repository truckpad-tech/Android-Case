package br.com.truckpad.waister.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.truckpad.waister.R
import br.com.truckpad.waister.api.Place
import br.com.truckpad.waister.domain.SearchPoint
import br.com.truckpad.waister.ui.activity.SearchLocationActivity

class PlacesAdapter(private val activity: Activity) :
    RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var data: List<Place> = ArrayList()

    fun setData(data: List<Place>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.item_places, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvPlace = itemView.findViewById<TextView>(R.id.tv_place)

        fun setData(place: Place) {
            tvPlace.text = place.display_name

            itemView.setOnClickListener {
                (activity as SearchLocationActivity).sendLocationResult(
//                    place.point[0], place.point[1], place.display_name
                    SearchPoint(place.display_name, place.point[0], place.point[1])
                )
            }
        }
    }

}