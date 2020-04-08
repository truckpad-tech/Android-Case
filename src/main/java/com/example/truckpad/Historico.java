package com.example.truckpad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.example.truckpad.data.Sqlite;
import com.example.truckpad.model.Rota;
import com.example.truckpad.view.Adapter_historico;

import java.util.ArrayList;

public class Historico extends AppCompatActivity {

    private Sqlite sqlite;
    private ArrayList<Rota> arrayList;
    private Adapter_historico adapter_historico;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        recyclerView = findViewById(R.id.recycler);
        sqlite = new Sqlite(Historico.this);
        arrayList = new ArrayList<Rota>();
        arrayList = (sqlite.gethistorico());
        //configura o recycler view e dispara a thread para pesquisar os fretes
        //usado exemplo de um recyclerview tbm como mais atual em relação ao listview
        configrecycler();

    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void configrecycler(){

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter_historico = new Adapter_historico(Historico.this,arrayList);
        recyclerView.setAdapter(adapter_historico);
    }

}
