package com.correia.app_truckpad.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_resultado.*
import com.correia.app_truckpad.R
import java.math.BigDecimal
import java.math.RoundingMode

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        var bundle :Bundle ?=intent.extras

        var origem: String = intent.getStringExtra("origem") // 2
        var destino: String = intent.getStringExtra("destino") // 2
        var eixo: String = intent.getStringExtra("eixo") // 2
        var distancia: String = intent.getStringExtra("distancia") // 2
        var duracao: String = intent.getStringExtra("duracao") // 2
        var pedagio: String = intent.getStringExtra("pedagio") // 2
        var combustivelNecessario: String = intent.getStringExtra("combustivel_necessario") // 2
        var totalComb: String = intent.getStringExtra("total_combustivel") // 2
        var total: String = intent.getStringExtra("total") // 2
        var frigorificada: String = intent.getStringExtra("frigorificada") // 2
        var geral: String = intent.getStringExtra("geral") // 2
        var granel: String = intent.getStringExtra("granel") // 2
        var neogranel: String = intent.getStringExtra("neogranel") // 2
        var perigosa: String = intent.getStringExtra("perigosa") // 2


        //Tratamento Origem
        tvOrigem.text = origem

        //Tratamento Destino
        tvDestino.text = destino

        //Tratamento Eixo
        tvEixos.text = eixo

        //Tratamento Distancia
        var distanciaDouble = distancia.toDouble()
        distanciaDouble = distanciaDouble/1000
        val distanciaDecimal = BigDecimal(distanciaDouble).setScale(2, RoundingMode.HALF_EVEN)
        var auxDistancia:String = distanciaDecimal.toString()
        val re = Regex("[^A-Za-z0-9 ]")
        auxDistancia = re.replace(auxDistancia, ",")
        tvDistancia.text = auxDistancia + " km"

        //Tratamento Duração
        var totalDuracao:Double = duracao.toDouble()
        var hours:Double = totalDuracao / 3600;
        var hoursInt: Int = hours.toInt()

        var minutes:Double = (totalDuracao % 3600) / 60;
        var minutesInt: Int = minutes.toInt()
        tvDuracao.text = hoursInt.toString() + " horas " + minutesInt.toString() + " Minutos"

        //Tratamento Pedagio
        var pedagioDecimal = BigDecimal(pedagio).setScale(2, RoundingMode.HALF_EVEN)
        var auxPedagio:String = pedagioDecimal.toString()
        auxPedagio = re.replace(auxPedagio, ",")
        tvPegadio.text = "R$" + auxPedagio

        //Tratamento Combustivel Necessário
        var combustivelNecessarioDecimal = BigDecimal(combustivelNecessario).setScale(2, RoundingMode.HALF_EVEN)
        var auxCombustivelNecessario:String = combustivelNecessarioDecimal.toString()
        auxCombustivelNecessario = re.replace(auxCombustivelNecessario, ",")
        tvCombNec.text = auxCombustivelNecessario + " L"

        //Tratamento Total Combustivel
        var totalCombDecimal = BigDecimal(totalComb).setScale(2, RoundingMode.HALF_EVEN)
        var auxTotalComb:String = totalCombDecimal.toString()
        auxTotalComb = re.replace(auxTotalComb, ",")
        tvTotalComb.text = "R$" + auxTotalComb

        //Tratamento Total
        var totalDecimal = BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN)
        var auxTotal:String = totalDecimal.toString()
        auxTotal = re.replace(auxTotal, ",")
        tvTotal.text = "R$" + auxTotal

        //Tratamento Frigorificada
        var frigorificadaDecimal = BigDecimal(frigorificada).setScale(2, RoundingMode.HALF_EVEN)
        var auxFrigorificada:String = frigorificadaDecimal.toString()
        auxFrigorificada = re.replace(auxFrigorificada, ",")
        tvFrigorificada.text = "R$"+auxFrigorificada+" + pedágio"

        //Tratamento Geral
        var geralDecimal = BigDecimal(geral).setScale(2, RoundingMode.HALF_EVEN)
        var auxGeral:String = geralDecimal.toString()
        auxGeral = re.replace(auxGeral, ",")
        tvGeral.text = "R$"+auxGeral+" + pedágio"

        //Tratamento Granel
        var granelDecimal = BigDecimal(geral).setScale(2, RoundingMode.HALF_EVEN)
        var auxGranel:String = granelDecimal.toString()
        auxGranel = re.replace(auxGranel, ",")
        tvGranel.text = "R$"+auxGranel+" + pedágio"

        //Tratamento Neogranel
        var neogranelDecimal = BigDecimal(neogranel).setScale(2, RoundingMode.HALF_EVEN)
        var auxNeogranel:String = neogranelDecimal.toString()
        auxNeogranel = re.replace(auxNeogranel, ",")
        tvNeogranel.text = "R$"+auxNeogranel+" + pedágio"

        //Tratamento Perigosa
        var perigosaDecimal = BigDecimal(perigosa).setScale(2, RoundingMode.HALF_EVEN)
        var auxPerigosa:String = perigosaDecimal.toString()
        auxPerigosa = re.replace(auxPerigosa, ",")
        tvPerigosa.text = "R$"+auxPerigosa+" + pedágio"

        buttonBack.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }


}
