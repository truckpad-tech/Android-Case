package com.isa.oliveira.truckerapp.Adapter

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isa.oliveira.truckerapp.Activity.MainActivity
import com.isa.oliveira.truckerapp.Fragment.HistoricoDetalheFragment
import com.isa.oliveira.truckerapp.Model.allDetails
import com.isa.oliveira.truckerapp.R
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class HistoricoAdapter : RecyclerView.Adapter<HistoricoAdapter.ViewHolder>() {


    private lateinit var mContext: Context
    private lateinit var container: LinearLayout

    private lateinit var final: TextView
    private lateinit var inicial: TextView



    private var allDetails= ArrayList<allDetails>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(
        context: Context,
        mySolicitations: ArrayList<allDetails>
    ){
        mContext = context
        this.allDetails = mySolicitations
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_historico, parent, false)

        final = itemView.findViewById(R.id.finall)
        inicial = itemView.findViewById(R.id.inicial)
        container = itemView.findViewById(R.id.container)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        var detail = allDetails[position]

        var  geocoder =  Geocoder(mContext, Locale.getDefault())

        try {

            var addresseInicial = geocoder.getFromLocation(detail!!.points!![0]!!.point[0], detail!!.points!![0]!!.point[1], 1);
            var addresseFinal = geocoder.getFromLocation(detail!!.points!![1]!!.point[0], detail!!.points!![1]!!.point[1], 1);

            final.text = addresseFinal[0].locality
            inicial.text = addresseInicial[0].locality



        }catch (ex: Exception){

        }
    container.setOnClickListener {
        var historicoDetalheFragment = HistoricoDetalheFragment()
        historicoDetalheFragment.add(detail, 0)
        (mContext as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.container, historicoDetalheFragment)
            .addToBackStack("historico")
            .commit()
    }

    }

    override fun getItemCount(): Int {
        return allDetails.size
    }
}