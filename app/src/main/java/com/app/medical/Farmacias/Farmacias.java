package com.app.medical.Farmacias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;

import java.util.ArrayList;

public class Farmacias extends AppCompatActivity {

    ArrayList<Farmacia_Model>listDatos;
    RecyclerView recycler;
    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmacias);

        listDatos = new ArrayList<>();

        recycler = (RecyclerView) findViewById(R.id.recycler_farmacias);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        llenarDatos();

        Adapter_Farmacia adapter = new Adapter_Farmacia(listDatos);
        
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Farmacias.this, "Horario Atención "+ listDatos
                        .get(recycler.getChildAdapterPosition(view)).getNombre()+" ", Toast.LENGTH_SHORT).show();
            }
        });
        
        recycler.setAdapter(adapter);

        btn_back = (ImageButton) findViewById(R.id.farmacia_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Farmacias.this, Menu_Activity.class));
            }
        });
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
