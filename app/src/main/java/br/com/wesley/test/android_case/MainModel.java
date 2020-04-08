package br.com.wesley.test.android_case;

import android.util.Log;

import br.com.wesley.test.android_case.model.AddressNominatim;
import br.com.wesley.test.android_case.model.Place;
import br.com.wesley.test.android_case.model.PlaceRoute;
import br.com.wesley.test.android_case.model.PlaceRouteResponse;
import br.com.wesley.test.android_case.model.PlacesApi;
import br.com.wesley.test.android_case.model.PrecoCarga;
import br.com.wesley.test.android_case.model.PrecoCargaResponse;
import br.com.wesley.test.android_case.service.CalculaFreteRetrofit;
import br.com.wesley.test.android_case.service.CalculaFreteRouteRetrofit;
import br.com.wesley.test.android_case.service.CalculaPrecoCargaRetrofit;
import br.com.wesley.test.android_case.service.NominatimRetrofit;
import br.com.wesley.test.android_case.service.NominatimService;
import br.com.wesley.test.android_case.service.TruckPadCargaService;
import br.com.wesley.test.android_case.service.TruckPadRouteService;
import br.com.wesley.test.android_case.service.TruckPadService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainModel implements MainContract.Model {

    private final TruckPadService truckPadService;
    private final NominatimService nominatimService;
    private MainPresenter presenter;

    public MainModel(MainPresenter presenter) {
        this.presenter = presenter;
        truckPadService = CalculaFreteRetrofit.getInstance().getTruckPadService();
        nominatimService = NominatimRetrofit.getInstance().getNominatimService();
    }

    @Override
    public void buscarLugares(String lugar, boolean origem) {
        Log.d("Main", "Model::buscarLugares");
        Call<PlacesApi> placesApiCall = truckPadService.getCities(lugar);
        placesApiCall.enqueue(new Callback<PlacesApi>() {
            @Override
            public void onResponse(Call<PlacesApi> call, Response<PlacesApi> response) {
                PlacesApi placesApi = response.body();
                if (placesApi != null && placesApi.getProvider() != null && placesApi.getPlaces() != null) {
                    Log.d("Main", placesApi.getProvider());
                    if (origem)
                        presenter.atualizaListaOrigem(placesApi.getPlaces());
                    else
                        presenter.atualizaListaDestino(placesApi.getPlaces());
                } else {
                    Log.d("Main", "places null");
                }
            }

            @Override
            public void onFailure(Call<PlacesApi> call, Throwable t) {
                Log.d("Main", "error", t);
            }
        });
    }

    @Override
    public void buscarInformacoesRotas(PlaceRoute placeRoute) {
        TruckPadRouteService truckPadRouteService = CalculaFreteRouteRetrofit.getInstance().getTruckPadRouteService();
        Call<PlaceRouteResponse> route = truckPadRouteService.getRoute(placeRoute);
        route.enqueue(new Callback<PlaceRouteResponse>() {
            @Override
            public void onResponse(Call<PlaceRouteResponse> call, Response<PlaceRouteResponse> response) {
                presenter.buscarPrecoCarga(response.body());
            }

            @Override
            public void onFailure(Call<PlaceRouteResponse> call, Throwable t) {
                Log.e("Main", t.getMessage(), t);
                presenter.mensagemError(t.getMessage());
            }
        });
    }

    @Override
    public void buscarInformacoesPrecoCarga(double distance, int axis) {
        TruckPadCargaService truckPadCargaService = CalculaPrecoCargaRetrofit.getInstance().getTruckPadCargaService();
        PrecoCarga precoCarga = new PrecoCarga();
        precoCarga.setAxis(axis);
        precoCarga.setDistance(distance / 1000);
        precoCarga.setHasReturnShipment(true);
        Call<PrecoCargaResponse> precoCargaResponseCall = truckPadCargaService.getPrecoCarga(precoCarga);
        precoCargaResponseCall.enqueue(new Callback<PrecoCargaResponse>() {
            @Override
            public void onResponse(Call<PrecoCargaResponse> call, Response<PrecoCargaResponse> response) {
                presenter.mostraInformacoesPrecoCarga(response.body());
            }

            @Override
            public void onFailure(Call<PrecoCargaResponse> call, Throwable t) {
                Log.e("Main", t.getMessage(), t);
                presenter.mensagemError(t.getMessage());
            }
        });
    }

    @Override
    public void buscarLocalizacaoAtual(double latitude, double longitude) {
        Log.d("Main", "Model::buscarLocalizacaoAtual");
        Call<AddressNominatim> nominatimCall = nominatimService.getAddress("json", latitude, longitude);
        nominatimCall.enqueue(new Callback<AddressNominatim>() {
            @Override
            public void onResponse(Call<AddressNominatim> call, Response<AddressNominatim> response) {
                AddressNominatim addressNominatim = response.body();
                if (addressNominatim != null) {
                    Place place = new Place();
                    place.setDisplayName(addressNominatim.getDisplayName());
                    place.setPoint(new double[]{latitude, longitude});
                    AddressNominatim.Address address = addressNominatim.getAddress();
                    if (address != null) {
                        place.setCity(address.getCity());
                        place.setAddress(address.getRoad());
                        place.setAreaCode(address.getPostcode());
                        place.setNeighborhood(address.getSuburb());
                        place.setState(address.getState());
                        place.setCountry(address.getCountry());
                    }
                    presenter.atualizarOrigemLocalizacaoAtual(place);
                } else {
                    Log.d("Main", "places null");
                }
            }

            @Override
            public void onFailure(Call<AddressNominatim> call, Throwable t) {
                Log.d("Main", "error", t);
                presenter.mensagemError("Erro ao consultar localização atual");
            }
        });
    }
}
