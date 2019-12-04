package com.isa.oliveira.truckerapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.isa.oliveira.truckerapp.Activity.MainActivity
import com.isa.oliveira.truckerapp.Adapter.HistoricoAdapter
import com.isa.oliveira.truckerapp.Model.allDetails
import com.isa.oliveira.truckerapp.Model.getParametros
import com.isa.oliveira.truckerapp.Model.getValues
import com.isa.oliveira.truckerapp.R


class HistoricoFragment : Fragment(){


    private var Eixo: Int = 0
    private lateinit var Lista1: getParametros
    private lateinit var Lista2: getValues

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_historico, container, false)

        auth = FirebaseAuth(FirebaseApp.initializeApp(context!!))
        var Main = MainActivity()

        var titulo = activity!!.findViewById<TextView>(R.id.txt_titulo)

        var his = activity!!.findViewById<TextView>(R.id.histo)
        var hisim = activity!!.findViewById<ImageView>(R.id.histoIm)
        var close = activity!!.findViewById<ImageView>(R.id.ico_back)

        var rotas = view.findViewById<TextView>(R.id.sem_rotas)
        close.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
        his.visibility = View.GONE
        titulo.setText("Historico Rotas")

       var recyclerView = view.findViewById<RecyclerView>(R.id.rv_recycler)

        var AllDetails = ArrayList<allDetails>()

        val dbPosts = FirebaseDatabase.getInstance().getReference().child(auth.currentUser!!.uid)
            .child("Rotas")
        AllDetails.clear()
        dbPosts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val c: allDetails = postSnapshot.getValue(allDetails::class.java)!!
                    AllDetails.add(c)
                }
                val HistoricoAdapter = HistoricoAdapter()

                HistoricoAdapter.addData(context!!, AllDetails)
                val layoutManager: RecyclerView.LayoutManager
                layoutManager = LinearLayoutManager(context)
                recyclerView.setLayoutManager(layoutManager)
                recyclerView.setItemAnimator(DefaultItemAnimator())
                recyclerView.setAdapter(HistoricoAdapter)

                if(AllDetails.isEmpty()){
                    rotas.visibility = VISIBLE
                    recyclerView.visibility = GONE
                }else{
                    rotas.visibility = GONE
                    recyclerView.visibility = VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


        return  view
    }

}
