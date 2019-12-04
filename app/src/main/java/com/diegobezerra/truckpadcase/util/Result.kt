package com.diegobezerra.truckpadcase.util

sealed class Result<out T : Any> {
    class Success<out T : Any>(val data: T) : Result<T>()
    object Error : Result<Nothing>()
}