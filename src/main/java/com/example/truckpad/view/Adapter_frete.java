package com.example.truckpad.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.truckpad.R;
import com.example.truckpad.model.Cidade;
import com.example.truckpad.model.Fretes;

import java.util.ArrayList;
import java.util.List;

public class Adapter_frete extends ArrayAdapter<Fretes> {

    private final Context context;
    private final List<Fretes> values;
    private ArrayList<Fretes> arraylist;

    public Adapter_frete(Context context, List<Fretes> values) {
        super(context, R.layout.frete, values);
        this.context = context;
        this.values = values;
        this.arraylist = new ArrayList<Fretes>();
        this.arraylist.addAll(values);

    }

    static class ViewHolder {

        TextView textVfrigorificada;
        TextView textVgeral;
        TextView textVgranel;
        TextView textVneogranel;
        TextView textVperigosa;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Adapter_frete.ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.frete, parent, false);

            viewHolder = new Adapter_frete.ViewHolder();
            viewHolder.textVfrigorificada = convertView.findViewById(R.id.textV_frigorif);
            viewHolder.textVgeral = convertView.findViewById(R.id.textV_geral);
            viewHolder.textVgranel =  convertView.findViewById(R.id.textV_granel);
            viewHolder.textVneogranel = convertView.findViewById(R.id.textV_neogranel);
            viewHolder.textVperigosa = convertView.findViewById(R.id.textV_perigosa);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (Adapter_frete.ViewHolder) convertView.getTag();
        }

        viewHolder.textVfrigorificada.setText(values.get(position).getFrigorificada());
        viewHolder.textVperigosa.setText(String.valueOf(values.get(position).getPerigosa()));
        viewHolder.textVneogranel.setText(String.valueOf(values.get(position).getNeogranel()));
        viewHolder.textVgranel.setText(String.valueOf(values.get(position).getGranel()));
        viewHolder.textVgeral.setText(String.valueOf(values.get(position).getGeral()));

        return convertView;
    }
}
