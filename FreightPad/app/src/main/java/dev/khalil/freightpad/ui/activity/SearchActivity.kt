package dev.khalil.freightpad.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.khalil.freightpad.R
import dev.khalil.freightpad.databinding.ActivitySearchBinding
import dev.khalil.freightpad.di.searchModule
import dev.khalil.freightpad.extensions.viewModel
import dev.khalil.freightpad.model.Place
import dev.khalil.freightpad.ui.adapter.LocationsAdapter
import dev.khalil.freightpad.ui.adapter.holder.LocationClick
import dev.khalil.freightpad.ui.viewModel.SearchActivityViewModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class SearchActivity : AppCompatActivity(), KodeinAware, LocationClick {

  override val kodein = Kodein.lazy {
    import(searchModule)
  }

  private lateinit var binding: ActivitySearchBinding

  private val locationsAdapter by lazy { LocationsAdapter(arrayListOf(), this) }

  private val searchViewModel: SearchActivityViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

    initObservers()
    initRecycler()
  }

  private fun initRecycler() {
    binding.placesRecycler.adapter = locationsAdapter
    binding.placesRecycler.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
  }

  private fun initObservers() {
    searchViewModel.searchResult.observe(this, Observer { locationList ->
      locationsAdapter.updateLocations(locationList)
    })



    binding.placeEdit.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        searchViewModel.search(s.toString())
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      }
    })
  }

  companion object {
    fun createIntent(context: Context): Intent {
      return Intent(context, SearchActivity::class.java)
    }
  }

  override fun locationClicked(place: Place) {
    Log.d("PLACECLICK", place.displayName)
  }
}