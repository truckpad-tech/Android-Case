package com.example.fretecalc.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fretecalc.R;
import com.example.fretecalc.models.routes.RouteSearch;
import com.example.fretecalc.utils.SingleClickListener;
import com.example.fretecalc.views.interfaces.HistoryListener;

import java.util.List;
import java.util.Locale;

import static com.example.fretecalc.utils.ConvertersUtils.distanceUnits;
import static com.example.fretecalc.utils.ConvertersUtils.timeUnits;

public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder> {
    private List<RouteSearch> routeSearches;
    private HistoryListener listener;

    public RecyclerHistoryAdapter(List<RouteSearch> routeSearches, HistoryListener listener) {
        this.routeSearches = routeSearches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHistoryAdapter.ViewHolder holder, int position) {
        RouteSearch routeSearch = routeSearches.get(position);

        holder.onBind(routeSearch);
    }

    @Override
    public int getItemCount() {
        return routeSearches == null ? 0 : routeSearches.size();
    }

    public void update(List<RouteSearch> responses) {
        this.routeSearches = responses;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textOrigin;
        private TextView textDestination;
        private TextView textTotalCost;
        private TextView textDistance;
        private TextView textDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrigin = itemView.findViewById(R.id.text_history_origin);
            textDestination = itemView.findViewById(R.id.text_history_destination);
            textTotalCost = itemView.findViewById(R.id.text_history_cost);
            textDistance = itemView.findViewById(R.id.text_history_distance);
            textDuration = itemView.findViewById(R.id.text_history_time);
        }

        public void onBind(RouteSearch routeSearch) {
            textOrigin.setText(routeSearch.getSearch().getOriginName());
            textDestination.setText(routeSearch.getSearch().getDestinationName());
            textTotalCost.setText(String.format(Locale.getDefault(), "R$ %.2f", routeSearch.getRouteResponse().getTotalCost()));
            textDistance.setText(String.format(Locale.getDefault(), "%.0f km", distanceUnits(routeSearch.getRouteResponse().getDistanceUnit(), routeSearch.getRouteResponse().getDistance())));
            textDuration.setText(String.format(Locale.getDefault(), "%s", timeUnits(routeSearch.getRouteResponse().getDurationUnit(), routeSearch.getRouteResponse().getDuration())));

            itemView.setOnClickListener(new SingleClickListener() {
                @Override
                public void onClicked(View v) {
                    listener.onHistoryClick(routeSearch.getSearch());
                }
            });

        }
    }

}
