package br.com.truckpad.waister.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import br.com.truckpad.waister.R
import br.com.truckpad.waister.api.ANTTPrice
import br.com.truckpad.waister.api.Calculate
import br.com.truckpad.waister.domain.History
import br.com.truckpad.waister.domain.SearchPoint
import br.com.truckpad.waister.ui.activity.ResultActivity.Companion.PARAM_EXTRA_ANTT_PRICE
import br.com.truckpad.waister.ui.activity.ResultActivity.Companion.PARAM_EXTRA_AXIS
import br.com.truckpad.waister.ui.activity.ResultActivity.Companion.PARAM_EXTRA_CALCULATE
import br.com.truckpad.waister.ui.activity.ResultActivity.Companion.PARAM_EXTRA_DESTINATION
import br.com.truckpad.waister.ui.activity.ResultActivity.Companion.PARAM_EXTRA_ORIGIN
import br.com.truckpad.waister.util.CurrencyTextWatcher
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_ORIGIN = 1
        const val REQUEST_DESTINATION = 2

        const val PARAM_EXTRA_FROM = "ParamExtraFrom"
        const val PARAM_EXTRA_POINT = "ParamExtraPoint"

        const val HAWK_HISTORY = "HawkHistory"
    }

    private lateinit var mOriginPoint: SearchPoint
    private lateinit var mDestinationPoint: SearchPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        et_origin.setOnClickListener {
            val intent = Intent(this, SearchLocationActivity::class.java)
            intent.putExtra(PARAM_EXTRA_FROM, REQUEST_ORIGIN)
            startActivityForResult(intent, REQUEST_ORIGIN)
        }

        et_destination.setOnClickListener {
            val intent = Intent(this, SearchLocationActivity::class.java)
            intent.putExtra(PARAM_EXTRA_FROM, REQUEST_DESTINATION)
            startActivityForResult(intent, REQUEST_DESTINATION)
        }

        et_fuel_consumption.addTextChangedListener(
            CurrencyTextWatcher(et_fuel_consumption)
        )

        et_fuel_price.addTextChangedListener(
            CurrencyTextWatcher(et_fuel_price, "R$")
        )

        bt_calculate.setOnClickListener {
            sendRoute()
        }

        et_fuel_price.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                bt_calculate.performClick()
                true
            } else {
                false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val point: SearchPoint? = data?.getParcelableExtra(PARAM_EXTRA_POINT)

        if (resultCode == Activity.RESULT_OK && point != null) {
            if (requestCode == REQUEST_ORIGIN) {

                et_origin.setText(point.name)

                mOriginPoint = point

            } else if (requestCode == REQUEST_DESTINATION) {

                et_destination.setText(point.name)

                mDestinationPoint = point

            }

            et_axis_quantity.requestFocus()
        }
    }

    private fun sendRoute() {
        val origin = et_origin.text.toString()
        val destination = et_destination.text.toString()
        val fuelConsumption = et_fuel_consumption.formatDouble()
        val fuelPrice = et_fuel_price.formatDouble()

        if (origin.isEmpty() || destination.isEmpty()) return

        hideKeyboard()

        pb_sending.visibility = View.VISIBLE
        bt_calculate.visibility = View.GONE

        val originArr = JSONArray()
            .put(0, mOriginPoint.latitude)
            .put(1, mOriginPoint.longitude)

        val destinationArr = JSONArray()
            .put(0, mDestinationPoint.latitude)
            .put(1, mDestinationPoint.longitude)

        val placesArr = JSONArray()
            .put(0, JSONObject().put("point", originArr))
            .put(1, JSONObject().put("point", destinationArr))

        val bodyObj = JSONObject()
        bodyObj.put("places", placesArr)
        bodyObj.put("fuel_consumption", fuelConsumption)
        bodyObj.put("fuel_price", fuelPrice)

        val url = "https://geo.api.truckpad.io/v1/route"
        val fuel = url.httpPost().body(bodyObj.toString()).timeout(60000)
        fuel.headers["Content-Type"] = "application/json"
        fuel.responseString { _, _, result ->
            val (data, error) = result

            if (!isFinishing) {
                if (error != null) {
                    pb_sending?.visibility = View.GONE
                    bt_calculate?.visibility = View.VISIBLE
                } else {

                    val calculate = Gson().fromJson(data, Calculate::class.java)

                    sendANTTPrice(calculate)
                }
            }
        }
    }

    private fun sendANTTPrice(calculate: Calculate) {
        val axisQuantity = et_axis_quantity.text.toString()

        val bodyObj = JSONObject()
        bodyObj.put("axis", axisQuantity)
        bodyObj.put("distance", (calculate.distance / 1000))
        bodyObj.put("has_return_shipment", "true")

        val url = "https://tictac.api.truckpad.io/v1/antt_price/all"
        val fuel = url.httpPost().body(bodyObj.toString()).timeout(60000)
        fuel.headers["Content-Type"] = "application/json"
        fuel.responseString { _, _, result ->
            val (data, error) = result

            pb_sending?.visibility = View.GONE
            bt_calculate?.visibility = View.VISIBLE

            if (error == null && !isFinishing) {
                val anttPrice = Gson().fromJson(data, ANTTPrice::class.java)

                val history = History(
                    et_origin.text.toString(),
                    et_destination.text.toString(),
                    axisQuantity,
                    calculate,
                    anttPrice
                )
                saveToHawk(history)

                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(PARAM_EXTRA_ORIGIN, history.origin)
                intent.putExtra(PARAM_EXTRA_DESTINATION, history.destination)
                intent.putExtra(PARAM_EXTRA_AXIS, history.axis)
                intent.putExtra(PARAM_EXTRA_CALCULATE, Gson().toJson(calculate).toString())
                intent.putExtra(PARAM_EXTRA_ANTT_PRICE, Gson().toJson(anttPrice).toString())
                startActivity(intent)
            }
        }
    }

    private fun saveToHawk(item: History) {
        val data: ArrayList<History>? = Hawk.get(HAWK_HISTORY, null)
        val list = data ?: ArrayList()

        list.add(item)

        Hawk.put(HAWK_HISTORY, list)
    }

    private fun AppCompatEditText.formatDouble(): Double {
        if (this.text != null && this.text.toString().isNotEmpty()) {
            return this.text.toString()
                .replace("[^\\d,]".toRegex(), "")
                .replace(",", ".")
                .toDouble()
        }
        return 0.0
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager

        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
