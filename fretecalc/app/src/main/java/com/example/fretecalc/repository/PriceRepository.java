package com.example.fretecalc.repository;

import android.content.Context;

import com.example.fretecalc.data.local.Database;
import com.example.fretecalc.data.local.PriceDao;
import com.example.fretecalc.models.prices.PriceRequest;
import com.example.fretecalc.models.prices.PriceResponse;

import io.reactivex.Observable;

import static com.example.fretecalc.data.remote.RetrofitService.getPricesService;

public class PriceRepository {
    public Observable<PriceResponse> getPrices(Long axis, Double distance, Boolean hasReturnShipment) {
        return getPricesService().postPrice(new PriceRequest(axis, distance, hasReturnShipment));
    }

    public void savePrices(Context context, Long id, PriceResponse priceResponse) {
        PriceDao priceDao = Database.getDatabase(context).priceDao();
        priceResponse.setPriceId(id);
        priceDao.insertPrice(priceResponse);
    }

    public Observable<PriceResponse> getPricesById(Context context, Long id) {
        PriceDao priceDao = Database.getDatabase(context).priceDao();
        return priceDao.pricesById(id);
    }
}
