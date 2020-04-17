package dev.khalil.freightpad.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.khalil.freightpad.api.repository.SearchLocationRepository
import dev.khalil.freightpad.model.Place
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SearchActivityViewModel(private val repository: SearchLocationRepository) : ViewModel() {

  private val searchResultLiveData = MutableLiveData<List<Place>>()
  val searchResult: LiveData<List<Place>>
    get() = searchResultLiveData

  private val _searchInput = BehaviorSubject.create<String>()
  private val searchInput = _searchInput.toFlowable(BackpressureStrategy.LATEST)

  private val compositeDisposable = CompositeDisposable()
  private var lastSearch = ""

  init {
    compositeDisposable.add(searchInput.debounce(500, TimeUnit.MILLISECONDS)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { executeSearch(it) }
    )
  }

  fun search(query: String) {
    if (query.length <= 3)
      return

    _searchInput.onNext(query)
  }

  private fun executeSearch(query: String) {
    if (lastSearch == query)
      return

    compositeDisposable.add(repository.searchLocation(query)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { responseList ->
        searchResultLiveData.value = responseList
        lastSearch = query
      })
  }

}
