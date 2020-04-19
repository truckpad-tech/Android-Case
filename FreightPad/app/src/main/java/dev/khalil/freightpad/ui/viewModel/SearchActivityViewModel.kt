package dev.khalil.freightpad.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.khalil.freightpad.api.repository.SearchApiRepository
import dev.khalil.freightpad.model.Place
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SearchActivityViewModel(private val repository: SearchApiRepository) : ViewModel() {

  private val searchResultLiveData = MutableLiveData<List<Place>>()
  val searchResult: LiveData<List<Place>>
    get() = searchResultLiveData

  private val errorMutableLiveData = MutableLiveData<Boolean>()
  val error: LiveData<Boolean>
    get() = errorMutableLiveData

  private val loadingMutableLiveData = MutableLiveData<Boolean>()
  val loading: LiveData<Boolean>
    get() = loadingMutableLiveData

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

    errorMutableLiveData.value = false
    loadingMutableLiveData.value = false
  }

  fun search(query: String) {
    if (query.length <= 3)
      return

    _searchInput.onNext(query)
  }

  fun onDestroy() {
    compositeDisposable.dispose()
    compositeDisposable.clear()
  }

  private fun executeSearch(query: String) {
    if (lastSearch == query)
      return

    compositeDisposable.add(
      repository.searchLocation(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe {
          loadingMutableLiveData.value = true
          errorMutableLiveData.value = false
        }
        .doAfterTerminate { loadingMutableLiveData.value = false }
        .subscribe({ responseList ->
          loadingMutableLiveData.value = false
          searchResultLiveData.value = responseList
          lastSearch = query
        }, {
          lastSearch = ""
          errorMutableLiveData.value = true
        }))
  }
}
