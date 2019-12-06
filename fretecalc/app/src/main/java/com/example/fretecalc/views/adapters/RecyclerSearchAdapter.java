package com.example.fretecalc.views.adapters;

import android.content.Context;
import android.text.style.CharacterStyle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fretecalc.R;
import com.example.fretecalc.utils.SingleClickListener;
import com.example.fretecalc.views.interfaces.PredictionListener;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> implements Filterable {
    private CharacterStyle STYLE_BOLD;
    private CharacterStyle STYLE_NORMAL;

    private List<AutocompletePrediction> predictionList = new ArrayList<>();
    private Context context;
    private PredictionListener listener;

    public RecyclerSearchAdapter(Context context, PredictionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if (charSequence != null) {
                    if (predictionList != null) {
                        results.values = predictionList;
                        results.count = predictionList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                }
            }
        };
    }

    public void clearPredictions() {
        predictionList.clear();
        notifyDataSetChanged();
    }

    public void updatePredictions(List<AutocompletePrediction> predictions) {
        this.predictionList = predictions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_place, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSearchAdapter.ViewHolder holder, int position) {
        AutocompletePrediction placePrediction = predictionList.get(position);
        holder.onBind(placePrediction);
    }

    @Override
    public int getItemCount() {
        return predictionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textMain;
        private TextView textDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textMain = itemView.findViewById(R.id.text_place_main);
            textDetail = itemView.findViewById(R.id.text_place_detail);
        }

        public void onBind(AutocompletePrediction placePrediction) {

            textMain.setText(placePrediction.getPrimaryText(STYLE_NORMAL));
            textDetail.setText(placePrediction.getFullText(STYLE_BOLD));

            itemView.setOnClickListener(new SingleClickListener() {
                @Override
                public void onClicked(View v) {
                    listener.onPredictionClick(placePrediction);
                }
            });
        }
    }
}
