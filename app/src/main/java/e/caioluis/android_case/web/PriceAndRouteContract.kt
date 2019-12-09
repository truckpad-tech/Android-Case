package e.caioluis.android_case.web

import e.caioluis.android_case.json.PriceResponse
import e.caioluis.android_case.json.RouteResponse

interface PriceAndRouteContract {

    fun routeResponseSuccess(modelResponse: RouteResponse)
    fun responseFailed()
    fun priceResponseSuccess(modelResponse: PriceResponse)
}
