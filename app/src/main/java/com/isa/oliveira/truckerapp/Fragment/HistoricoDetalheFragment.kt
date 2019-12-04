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
import com.isa.oliveira.truckerapp.Model.allDetails
import com.isa.oliveira.truckerapp.Model.getParametros
import com.isa.oliveira.truckerapp.Model.getValues
import com.isa.oliveira.truckerapp.R


class HistoricoDetalheFragment : Fragment(){


    private var Eixo: Int = 0
    private lateinit var allDetails: allDetails
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
    private lateinit var auth: FirebaseAuth

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
            auth.signOut()
            val i = Intent(activity, LoginActivity::class.java)
            startActivity(i)
            activity!!.finish()
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

        titulo.setText("Detalhe")


      //  origem.setText(allDetails.)
        combustivel.text = allDetails.fuel_usage_unit+ " " + allDetails.fuel_usage
      //  destino = view.findViewById(R.id.destino)
        distancia.text = allDetails.distance.toString() + " " + allDetails.distance_unit
        duracao.text =allDetails.duration.toString() + " " +  allDetails.duration_unit
        eixos.text = Eixo.toString()
        total.text = allDetails.total_cost.toString()
        perigosa.text = "R$ " + allDetails.perigosa
        pedagio.text = allDetails.toll_count.toString()  + "  +  Valor total dos pedagios  " + allDetails.toll_cost_unit + " " +allDetails.toll_cost
        geral.text = "R$ " + allDetails.geral
        granel.text = "R$ " + allDetails.granel
        neogranel.text = "R$ " + allDetails.neogranel
        frigorificada.text = "R$ " + allDetails.frigorificada

        return  view
    }

    fun add(allDetails: allDetails, eixo : Int){

        val serializer = Gson()
        
        this.allDetails = allDetails
        Eixo = eixo
    }

}
