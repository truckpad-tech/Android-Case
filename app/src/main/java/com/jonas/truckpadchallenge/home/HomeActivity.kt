package com.jonas.truckpadchallenge.home

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jonas.truckpadchallenge.R
import com.jonas.truckpadchallenge.history.HistoryFragment
import com.jonas.truckpadchallenge.search.presentation.SearchFragment
import kotlinx.android.synthetic.main.activity_home.bottom_navigation_home

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val searchFragment = SearchFragment()
    private val historyFragment = HistoryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottom_navigation_home.setOnNavigationItemSelectedListener(this)

        showFragment(searchFragment)

        if (checkLocationPermission()) requestPermissions()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> showFragment(searchFragment)
            R.id.history -> showFragment(historyFragment)
        }
        item.isChecked = true
        return true
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_home, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun checkLocationPermission() =
        checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED

    private fun requestPermissions() {
        if (VERSION.SDK_INT >= VERSION_CODES.M)
            requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
    }

    companion object {
        const val LOCATION_REQUEST_CODE = 1
    }
}