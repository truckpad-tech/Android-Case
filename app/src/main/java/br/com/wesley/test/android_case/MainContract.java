package br.com.wesley.test.android_case;

import android.view.View;

import java.util.List;

import br.com.wesley.test.android_case.model.Place;
import br.com.wesley.test.android_case.model.PlaceRoute;
import br.com.wesley.test.android_case.model.PlaceRouteResponse;
import br.com.wesley.test.android_case.model.PrecoCargaResponse;

public interface MainContract {
    interface View {
        void atualizaListaOrigem(List<Place> places);
        void atualizaListaDestino(List<Place> places);
        void atualizaQuantidadeEixos(int eixos);
        void atualizarOrigemLocalizacaoAtual(Place place);
        void abrirConfiguracoesHabilitarLocalizacao();
        void mostraInformacoesRotas(PlaceRouteResponse placeRouteResponse);
        void mostraInformacoesPrecoCarga(PrecoCargaResponse precoCargaResponse);
        void mensagemError(String error);
        void showPregressBar(String mensagem);
        void showPregressBar();
        void hidePregressBar();
        void showProgressBarCarregandoListaLugares();
        void hideProgressBarCarregandoListaLugares();
    }

    interface Presenter {
        void buscarOrigem(String lugar);
        void buscarDestino(String lugar);
        void atualizaListaOrigem(List<Place> places);
        void atualizaListaDestino(List<Place> places);
        void maisEixos(String eixos);
        void menosEixos(String eixos);
        void buscarLocalizacaoAtual(double latitude, double longitude);
        void atualizarOrigemLocalizacaoAtual(Place place);
        void buscarInformacoesRotas(PlaceRoute placeRoute);
        void buscarPrecoCarga(PlaceRouteResponse placeRouteResponse);
        void mostraInformacoesPrecoCarga(PrecoCargaResponse precoCargaResponse);
        void buscarInformacoesPrecoCarga(double distance, int axis);
        void mensagemError(String error);
    }

    interface Model {
        void buscarLugares(String lugar, boolean origem);
        void buscarInformacoesRotas(PlaceRoute placeRoute);
        void buscarInformacoesPrecoCarga(double distance, int axis);
        void buscarLocalizacaoAtual(double latitude, double longitude);
    }
}
