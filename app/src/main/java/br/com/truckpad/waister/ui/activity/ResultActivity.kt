package br.com.truckpad.waister.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.com.truckpad.waister.R
import br.com.truckpad.waister.api.ANTTPrice
import br.com.truckpad.waister.api.Calculate
import br.com.truckpad.waister.util.formatDistance
import br.com.truckpad.waister.util.formatFuel
import br.com.truckpad.waister.util.formatPrice
import br.com.truckpad.waister.util.formatTime
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_result.*
import java.util.*


class ResultActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val PARAM_EXTRA_ORIGIN = "ParamExtraOrigin"
        const val PARAM_EXTRA_DESTINATION = "ParamExtraDestination"
        const val PARAM_EXTRA_AXIS = "ParamExtraAxis"
        const val PARAM_EXTRA_CALCULATE = "ParamExtraCalculate"
        const val PARAM_EXTRA_ANTT_PRICE = "ParamExtraANTTPrice"
    }

    private var mMap: GoogleMap? = null
    private lateinit var mOriginName: String
    private lateinit var mDestinationName: String
    private lateinit var mAxis: String
    private lateinit var mCalculate: Calculate
    private lateinit var mAnttPrice: ANTTPrice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mOriginName = intent?.getStringExtra(PARAM_EXTRA_ORIGIN) ?: ""
        mDestinationName = intent?.getStringExtra(PARAM_EXTRA_DESTINATION) ?: ""
        mAxis = intent?.getStringExtra(PARAM_EXTRA_AXIS) ?: ""
        val calculateStr = intent?.getStringExtra(PARAM_EXTRA_CALCULATE)
        val anttPriceStr = intent?.getStringExtra(PARAM_EXTRA_ANTT_PRICE)

        if (calculateStr != null && anttPriceStr != null) {
            mCalculate = Gson().fromJson(calculateStr, Calculate::class.java)
            mAnttPrice = Gson().fromJson(anttPriceStr, ANTTPrice::class.java)

            initViews()
        } else {
            tv_error_params.visibility = View.VISIBLE
        }
    }

    private fun initViews() {
        renderMaps()

        ll_result.removeAllViews()

        printLine(R.string.source, mOriginName)
        printLine(R.string.destiny, mDestinationName)
        printLine(R.string.axis, mAxis)
        printLine(R.string.distance, mCalculate.distance.formatDistance(mCalculate.distance_unit))
        printLine(R.string.duration, mCalculate.duration.formatTime(mCalculate.duration_unit))
        printLine(R.string.toll, mCalculate.toll_cost.formatPrice())
        printLine(R.string.necessary_fuel, mCalculate.fuel_usage.formatFuel())
        printLine(R.string.total_fuel, mCalculate.fuel_cost.formatPrice())
        printLine(R.string.total, mCalculate.total_cost.formatPrice())

        printHeader()

        printLine(
            R.string.general,
            getString(R.string.price_toll, mAnttPrice.general.formatPrice())
        )
        printLine(R.string.bulk, getString(R.string.price_toll, mAnttPrice.bulk.formatPrice()))
        printLine(
            R.string.neo_bulk,
            getString(R.string.price_toll, mAnttPrice.neo_bulk.formatPrice())
        )
        printLine(
            R.string.refrigerated,
            getString(R.string.price_toll, mAnttPrice.refrigerated.formatPrice())
        )
        printLine(
            R.string.dangerous,
            getString(R.string.price_toll, mAnttPrice.dangerous.formatPrice())
        )
    }

    private fun printLine(keyRes: Int, value: String) {
        val lineView: View = View.inflate(this, R.layout.item_result_line, null)

        lineView.findViewById<TextView>(R.id.tv_key).setText(keyRes)
        lineView.findViewById<TextView>(R.id.tv_value).text = value

        ll_result.addView(lineView)
    }

    private fun printHeader() {
        ll_result.addView(
            View.inflate(this, R.layout.item_result_header, null)
        )
    }

    private fun renderMaps() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fr_maps) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        if (mMap != null) {
            mMap!!.clear()

            val origin = mCalculate.points[0].point
            val originPoint = LatLng(origin[1], origin[0])

            val destination = mCalculate.points[1].point
            val destinationPoint = LatLng(destination[1], destination[0])

            mMap!!.addMarker(
                MarkerOptions()
                    .position(originPoint)
                    .title(getString(R.string.origin_city))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shipment_origin))
            )

            mMap!!.addMarker(
                MarkerOptions()
                    .position(destinationPoint)
                    .title(getString(R.string.destination_city))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shipment_destination))
            )

            val points = ArrayList<LatLng>()

            mCalculate.route.forEach {
                it.forEach { point ->
                    points.add(LatLng(point[1], point[0]))
                }
            }

            mMap!!.addPolyline(
                PolylineOptions()
                    .addAll(points)
            )

            if (points.size >= 2) {
                mMap!!.setOnMapLoadedCallback {
                    val builder = LatLngBounds.Builder()
                    points.forEach {
                        builder.include(it)
                    }
                    val update = CameraUpdateFactory.newLatLngBounds(builder.build(), 80)
                    mMap!!.moveCamera(update)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}
