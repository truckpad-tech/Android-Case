package com.jonas.truckpadchallenge.core.utils

import android.view.View
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T : Any> Maybe<out T>.subscribeOnIO(): Maybe<out T> {
    return subscribeOn(Schedulers.io())
}

fun <T : Any> Maybe<out T>.observeOnMain(): Maybe<out T> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}