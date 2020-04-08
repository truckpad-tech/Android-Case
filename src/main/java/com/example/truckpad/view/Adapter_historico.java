package com.example.truckpad.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.truckpad.R;
import com.example.truckpad.model.Fretes;
import com.example.truckpad.model.Rota;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter_historico extends RecyclerView.Adapter<Adapter_historico.MyViewHolder> {

    private final Context context;
    private List<Rota> arraylist;

    public Adapter_historico(Context mContext, List<Rota> rotaList) {

        this.context = mContext;
        this.arraylist = rotaList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textVieworigem;
        public TextView textViewdestino;
        public TextView textViewvalor;
        public TextView textViewdistancia;
        public TextView textViewcomb;


        public MyViewHolder(View view) {
            super(view);
            textVieworigem = view.findViewById(R.id.tv_historigem);
            textViewdestino =  view.findViewById(R.id.tv_histdestino);
            textViewvalor =  view.findViewById(R.id.tv_histvalor);
            textViewdistancia = view.findViewById(R.id.tv_histdistancia);
            textViewcomb = view.findViewById(R.id.tv_histcombust);

        }

        @Override
        public void onClick(View v) {

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historico, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.textViewcomb.setText(String.valueOf(arraylist.get(position).getCombustivel_usado()));
        holder.textViewvalor.setText(String.valueOf(arraylist.get(position).getTotal_cost()));
        holder.textViewdestino.setText(String.valueOf(arraylist.get(position).getCiddestino()));
        holder.textVieworigem.setText(String.valueOf(arraylist.get(position).getCidorigem()));
        holder.textViewdistancia.setText(String.valueOf(arraylist.get(position).getDistancia()));

    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

}
