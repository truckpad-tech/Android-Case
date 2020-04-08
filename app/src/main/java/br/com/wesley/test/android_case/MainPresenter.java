package br.com.wesley.test.android_case;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import br.com.wesley.test.android_case.model.Place;
import br.com.wesley.test.android_case.model.PlaceRoute;
import br.com.wesley.test.android_case.model.PlaceRouteResponse;
import br.com.wesley.test.android_case.model.PrecoCargaResponse;

public class MainPresenter implements MainContract.Presenter {


    private MainActivity view;
    private MainModel model;

    public MainPresenter(MainActivity view) {
        this.view = view;
        this.model = new MainModel(this);
    }

    @Override
    public void buscarOrigem(String lugar) {
        Log.d("Main", "Presenter::buscarOrigem");
        this.model.buscarLugares(lugar, true);
    }

    @Override
    public void buscarDestino(String lugar) {
        Log.d("Main", "Presenter::buscarDestino");
        this.model.buscarLugares(lugar, false);
    }

    @Override
    public void atualizaListaOrigem(List<Place> places) {
        Log.d("Main", "Presenter::atualizaListaOrigem");
        this.view.atualizaListaOrigem(places);
    }

    @Override
    public void atualizaListaDestino(List<Place> places) {
        Log.d("Main", "Presenter::atualizaListaDestino");
        this.view.atualizaListaDestino(places);
    }

    @Override
    public void maisEixos(String eixos) {
        int quantidadeEixos = 2;
        if (!TextUtils.isEmpty(eixos)) {
            int eixosAtual = Integer.parseInt(eixos);
            quantidadeEixos = (eixosAtual < 9) ? eixosAtual + 1 : eixosAtual;
        }
        this.view.atualizaQuantidadeEixos(quantidadeEixos);
    }

    @Override
    public void menosEixos(String eixos) {
        int quantidadeEixos = 2;
        if (!TextUtils.isEmpty(eixos)) {
            int eixosAtual = Integer.parseInt(eixos);
            quantidadeEixos = (eixosAtual > 2) ? eixosAtual - 1 : eixosAtual;
        }
        this.view.atualizaQuantidadeEixos(quantidadeEixos);
    }

    @Override
    public void buscarLocalizacaoAtual(double latitude, double longitude) {
        this.model.buscarLocalizacaoAtual(latitude, longitude);
    }

    @Override
    public void atualizarOrigemLocalizacaoAtual(Place place) {
        this.view.hidePregressBar();
        this.view.atualizarOrigemLocalizacaoAtual(place);
    }

    @Override
    public void buscarInformacoesRotas(PlaceRoute placeRoute) {
        this.view.showPregressBar();
        this.model.buscarInformacoesRotas(placeRoute);
    }

    @Override
    public void buscarPrecoCarga(PlaceRouteResponse placeRouteResponse) {
        this.view.mostraInformacoesRotas(placeRouteResponse);
    }

    @Override
    public void mostraInformacoesPrecoCarga(PrecoCargaResponse precoCargaResponse) {
        this.view.hidePregressBar();
        this.view.mostraInformacoesPrecoCarga(precoCargaResponse);
    }

    @Override
    public void buscarInformacoesPrecoCarga(double distance, int axis) {
        this.model.buscarInformacoesPrecoCarga(distance, axis);
    }

    @Override
    public void mensagemError(String error) {
        this.view.hidePregressBar();
        this.view.mensagemError(error);
    }
}
