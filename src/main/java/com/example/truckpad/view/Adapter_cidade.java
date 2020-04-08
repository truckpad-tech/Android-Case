package com.example.truckpad.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.truckpad.R;
import com.example.truckpad.model.Cidade;

import java.util.ArrayList;
import java.util.List;

public class Adapter_cidade extends ArrayAdapter<Cidade> {
    private final Context context;
    private final List<Cidade> values;
    private ArrayList<Cidade> arraylist;

    public Adapter_cidade(Context context, List<Cidade> values) {
        super(context, R.layout.cidade, values);
        this.context = context;
        this.values = values;
        this.arraylist = new ArrayList<Cidade>();
        this.arraylist.addAll(values);

    }
    static class ViewHolder {

        TextView textVcidade;
        TextView textVbairro;
        TextView textVrua;
        TextView textVuf;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Adapter_cidade.ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cidade, parent, false);

            viewHolder = new Adapter_cidade.ViewHolder();
            viewHolder.textVcidade = convertView.findViewById(R.id.textV_nomecid);
            viewHolder.textVbairro = convertView.findViewById(R.id.textV_bairrocid);
            viewHolder.textVrua =  convertView.findViewById(R.id.textV_ruacid);
            viewHolder.textVuf = convertView.findViewById(R.id.textV_ufcid);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (Adapter_cidade.ViewHolder) convertView.getTag();
        }

        viewHolder.textVcidade.setText(values.get(position).getNome());
        viewHolder.textVbairro.setText(String.valueOf(values.get(position).getBairro()));
        viewHolder.textVrua.setText(String.valueOf(values.get(position).getLogradouro()));
        viewHolder.textVuf.setText(String.valueOf(values.get(position).getUf()));

        return convertView;
    }
}
