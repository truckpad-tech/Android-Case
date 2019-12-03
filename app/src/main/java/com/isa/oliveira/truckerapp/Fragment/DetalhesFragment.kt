package com.isa.oliveira.truckerapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.isa.oliveira.truckerapp.Activity.LoginActivity
import com.isa.oliveira.truckerapp.Activity.MainActivity
import com.isa.oliveira.truckerapp.Model.getParametros
import com.isa.oliveira.truckerapp.Model.getValues
import com.isa.oliveira.truckerapp.R


class DetalhesFragment : Fragment(){


    private var Eixo: Int = 0
    private lateinit var Lista1: getParametros
    private lateinit var Lista2: getValues
    private lateinit var origem : TextView
    private lateinit var combustivel : TextView
    private lateinit var destino : TextView
    private lateinit var distancia : TextView
    private lateinit var duracao : TextView
    private lateinit var eixos : TextView
    private lateinit var total : TextView
    private lateinit var perigosa : TextView

    private lateinit var pedagio : TextView
    private lateinit var geral : TextView
    private lateinit var granel : TextView
    private lateinit var neogranel : TextView
    private  lateinit  var auth: FirebaseAuth
    private lateinit var frigorificada : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_detalhes, container, false)
        auth = FirebaseAuth(FirebaseApp.initializeApp(context!!))
        origem = view.findViewById(R.id.origem)
        combustivel = view.findViewById(R.id.combustivel)
        destino = view.findViewById(R.id.destino)
        distancia = view.findViewById(R.id.distancia)
        duracao = view.findViewById(R.id.duracao)
        eixos = view.findViewById(R.id.eixos)
        total = view.findViewById(R.id.total)
        perigosa = view.findViewById(R.id.perigosa)
        pedagio = view.findViewById(R.id.pedagio)
        geral = view.findViewById(R.id.geral)
        granel = view.findViewById(R.id.granel)
        neogranel = view.findViewById(R.id.neogranel)
        frigorificada = view.findViewById(R.id.frigorificada)

        var Main = MainActivity()

        var titulo = activity!!.findViewById<TextView>(R.id.txt_titulo)

        var his = activity!!.findViewById<TextView>(R.id.histo)
        var hisim = activity!!.findViewById<ImageView>(R.id.histoIm)
        var close = activity!!.findViewById<ImageView>(R.id.ico_back)


        close.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
        if (auth.currentUser != null){
            hisim.setOnClickListener {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HistoricoFragment())
                    .addToBackStack("")
                    .commit()
            }
            his.setOnClickListener {
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HistoricoFragment())
                    .addToBackStack("")
                    .commit()
            }
            his.visibility = View.VISIBLE

        }else{
            hisim.setOnClickListener {
                Toast.makeText(context, "Logue para acessar seu historico", Toast.LENGTH_SHORT).show()
            }
            his.setOnClickListener {
                Toast.makeText(context, "Logue para acessar seu historico", Toast.LENGTH_SHORT).show()
            }
            his.visibility = View.VISIBLE
        }


        titulo.setText("Rota Resultado")

      //  origem.setText(Lista1.)
        combustivel.text = Lista1.fuel_usage_unit+ " " + Lista1.fuel_usage
      //  destino = view.findViewById(R.id.destino)
        distancia.text = Lista1.distance.toString() + " " + Lista1.distance_unit
        duracao.text =Lista1.duration.toString() + " " +  Lista1.duration_unit
        eixos.text = Eixo.toString()
        total.text = Lista1.total_cost.toString()
        perigosa.text = "R$ " + Lista2.perigosa
        pedagio.text = Lista1.toll_count.toString()  + "  +  Valor total dos pedagios  " + Lista1.toll_cost_unit + " " +Lista1.toll_cost
        geral.text = "R$ " + Lista2.geral
        granel.text = "R$ " + Lista2.granel
        neogranel.text = "R$ " + Lista2.neogranel
        frigorificada.text = "R$ " + Lista2.frigorificada

        return  view
    }

    fun add(result1 : String, result2: String, eixo : Int){

        val serializer = Gson()
         Lista2 = serializer.fromJson(result2, getValues::class.java)
         Lista1 = serializer.fromJson(result1, getParametros::class.java)
        Eixo = eixo
    }

}
