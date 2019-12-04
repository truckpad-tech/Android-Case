package com.diegobezerra.truckpadcase.util

import retrofit2.Response

suspend fun <T : Any> safeRequest(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            Result.Success(response.body()!!)
        } else {
            Result.Error
        }
    } catch (e: Exception) {
        Result.Error
    }
}