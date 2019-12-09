package e.caioluis.android_case.web

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface WepApiService {

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("v1/route")
    fun createRoutePost(@Body model: RequestBody): Call<ResponseBody>

    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("v1/antt_price/all")
    fun createPricePost(@Body model: RequestBody): Call<ResponseBody>
}