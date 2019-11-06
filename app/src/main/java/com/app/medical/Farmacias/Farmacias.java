package com.app.medical.Farmacias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.medical.R;

import java.util.ArrayList;

public class Farmacias extends AppCompatActivity {

    ArrayList<Farmacia_Model>listDatos;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmacias);

        listDatos = new ArrayList<>();

        recycler = (RecyclerView) findViewById(R.id.recycler_farmacias);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        llenarDatos();

        Adapter_Farmacia adapter = new Adapter_Farmacia(listDatos);
        recycler.setAdapter(adapter);
    }

    private void llenarDatos() {
        listDatos.add(new Farmacia_Model("Farmacia 1","Dirección", R.drawable.farmacia));
        listDatos.add(new Farmacia_Model("Farmacia 2","Dirección", R.drawable.farmacia));
        listDatos.add(new Farmacia_Model("Farmacia 3","Dirección", R.drawable.farmacia));
        listDatos.add(new Farmacia_Model("Farmacia 4","Dirección", R.drawable.farmacia));
        listDatos.add(new Farmacia_Model("Farmacia 5","Dirección", R.drawable.farmacia));
        listDatos.add(new Farmacia_Model("Farmacia 6","Dirección", R.drawable.farmacia));
    }
}
