package e.caioluis.android_case.web

import com.google.gson.Gson
import e.caioluis.android_case.json.PriceEnv
import e.caioluis.android_case.json.PriceResponse
import e.caioluis.android_case.json.RouteEnv
import e.caioluis.android_case.json.RouteResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriceAndRouteInteractor(val contract: PriceAndRouteContract) {

    var gson: Gson = Gson()

    fun sendRequest(routeEnvModel: RouteEnv? = null, priceEnvModel: PriceEnv? = null) {

        var body: RequestBody = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            gson.toJson(routeEnvModel)
        )

        if (priceEnvModel != null) {

            body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(priceEnvModel)
            )

            ApiService.priceService.createPricePost(body).enqueue(object : Callback<ResponseBody> {

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    contract.responseFailed()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    if (response.isSuccessful) {

                        contract.priceResponseSuccess(
                            gson.fromJson(
                                response.body()?.string(),
                                PriceResponse::class.java
                            )
                        )
                    } else {

                        contract.responseFailed()
                    }
                }
            })

            return
        }

        ApiService.routeService.createRoutePost(body).enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                contract.responseFailed()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {

                    contract.routeResponseSuccess(
                        gson.fromJson(
                            response.body()?.string(),
                            RouteResponse::class.java
                        )
                    )

                } else {

                    contract.responseFailed()
                }
            }
        })

    }
}