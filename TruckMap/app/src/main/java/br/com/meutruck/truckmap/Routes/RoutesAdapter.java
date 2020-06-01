package br.com.meutruck.truckmap.Routes;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.meutruck.truckmap.ActivityDetalRoute;
import br.com.meutruck.truckmap.R;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesHolder> {

    public final List<Route> routes;
    public RoutesAdapter(List<Route> routes) {
        this.routes = routes;
    }

    View v;

    @Override
    public RoutesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RoutesHolder( LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_historic, parent, false));
    }

    @Override
    public void onBindViewHolder(final RoutesHolder holder, final int position) {
        holder.textDate.setText(routes.get(position).getDate());
        holder.textOrigem.setText(routes.get(position).getOrigin());
        holder.textDestination.setText(routes.get(position).getDestination());

        holder.item_historic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(v), ActivityDetalRoute.class);
                it.putExtra("id", routes.get(position).getId());
                it.putExtra("eixos", routes.get(position).getEixos());
                it.putExtra("tollCount", routes.get(position).getTollCount());
                it.putExtra("km", routes.get(position).getDistance());
                it.putExtra("time", routes.get(position).getDuration());
                it.putExtra("fuelKm", routes.get(position).getFuelKm());
                it.putExtra("fuelValue", routes.get(position).getFuelValue());
                it.putExtra("tollCost", routes.get(position).getTollCost());
                it.putExtra("fuelUsage", routes.get(position).getFuelUsage());
                it.putExtra("tollCount", routes.get(position).getTollCount());
                it.putExtra("originPositionLatitude", routes.get(position).getOriginPositionLatitude());
                it.putExtra("originPositionLongitude", routes.get(position).getOriginPositionLongitude());
                it.putExtra("destinationPositionLatitude", routes.get(position).getDestinationPositionLatitude());
                it.putExtra("destinationPositionLongitude", routes.get(position).getDestinationPositionLongitude());
                it.putExtra("hasTolls", routes.get(position).getHasTolls());
                it.putExtra("tollCostUnit", "R$");
                it.putExtra("fuelCost", routes.get(position).getFuelCost());
                it.putExtra("locationOrigin", routes.get(position).getOrigin());
                it.putExtra("locationDestination", routes.get(position).getDestination());
                it.putExtra("priceFrigorificada", routes.get(position).getPriceFrigorificada());
                it.putExtra("priceGeral", routes.get(position).getPriceGeral());
                it.putExtra("priceGranel", routes.get(position).getPriceGranel());
                it.putExtra("priceNeogranel", routes.get(position).getPriceNeogranel());
                it.putExtra("pricePerigosa", routes.get(position).getPricePerigosa());
                it.putExtra("page", "Histórico de pesquisa"
                );
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getActivity(v).startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes != null ? routes.size() : 0;
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

}
