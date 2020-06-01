package br.com.meutruck.truckmap;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.meutruck.truckmap.Routes.ControllerRoutes;
import br.com.meutruck.truckmap.Routes.Route;
import br.com.meutruck.truckmap.Routes.RoutesAdapter;

public class ActivityHistoric extends AppCompatActivity {

    private RecyclerView recycler;
    private LinearLayout layoutBack, layoutNoRoute;
    private TextView textNoRoute, textPage;
    private RoutesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        declaration();
        recyclerHistoric();


        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void recyclerHistoric() {
        recycler = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL,true);
        recycler.setLayoutManager(layoutManager);

        adapter = new RoutesAdapter(procurarAtividades());
        recycler.setAdapter(adapter);
    }

    private List<Route> routes = new ArrayList<>();
    private List<Route> procurarAtividades(){
        ControllerRoutes control = new ControllerRoutes(this);
        int count = 0;
        for(int i = 0; i < control.returnRoute().size(); i++) {
            routes.add(new Route(i, control.returnRoute().get(i).getEixos(), control.returnRoute().get(i).getTollCount(),
                    control.returnRoute().get(i).getDistance(), control.returnRoute().get(i).getDuration(), control.returnRoute().get(i).getFuelKm(),
                    control.returnRoute().get(i).getFuelValue(), control.returnRoute().get(i).getTollCost(), control.returnRoute().get(i).getFuelUsage(),
                    control.returnRoute().get(i).getFuelCost(), control.returnRoute().get(i).getOriginPositionLatitude(), control.returnRoute().get(i).getOriginPositionLongitude(),
                    control.returnRoute().get(i).getDestinationPositionLatitude(), control.returnRoute().get(i).getDestinationPositionLongitude(),
                    control.returnRoute().get(i).getPriceFrigorificada(), control.returnRoute().get(i).getPriceGeral(), control.returnRoute().get(i).getPriceGranel(),
                    control.returnRoute().get(i).getPriceNeogranel(), control.returnRoute().get(i).getPricePerigosa(), control.returnRoute().get(i).getHasTolls(),
                    control.returnRoute().get(i).getOrigin(), control.returnRoute().get(i).getDestination(), control.returnRoute().get(i).getDate()));
        count++;
        }
        if(count == 0){
            showNoRoute();
        }
        return routes;
    }

    private void showNoRoute() {
        ViewGroup.LayoutParams params = layoutNoRoute.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutNoRoute.setLayoutParams(params);
    }

    public void declaration(){
        layoutBack = findViewById(R.id.layoutBack);
        layoutNoRoute = findViewById(R.id.layoutNoRoute);
        layoutBack = findViewById(R.id.layoutBack);
        textNoRoute = findViewById(R.id.textNoRoute);
        textPage = findViewById(R.id.textPage);

        Typeface fontBold = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Bold.ttf");
        Typeface fontMedium = Typeface.createFromAsset(getBaseContext().getAssets(),"Spartan-Medium.ttf");

        textPage.setTypeface(fontBold);
        textNoRoute.setTypeface(fontMedium);
    }
}
