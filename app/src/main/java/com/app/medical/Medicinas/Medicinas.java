package com.app.medical.Medicinas;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.app.medical.R;

import java.util.ArrayList;

public class Medicinas extends AppCompatActivity {

    ArrayList<Medicina_model> listDatos;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listDatos = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.recycler_medicinas);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        rellenarLista();

        Medicina_Adapter adapter = new Medicina_Adapter(listDatos);
        recycler.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void rellenarLista() {
        listDatos.add(new Medicina_model("Medicina 1","Dosis 1",R.drawable.baseline_account_circle_white_18dp));
        listDatos.add(new Medicina_model("Medicina 2","Dosis 2",R.drawable.baseline_account_circle_white_18dp));
        listDatos.add(new Medicina_model("Medicina 3","Dosis 3",R.drawable.baseline_account_circle_white_18dp));
        listDatos.add(new Medicina_model("Medicina 4","Dosis 4",R.drawable.baseline_account_circle_white_18dp));
        listDatos.add(new Medicina_model("Medicina 5","Dosis 5",R.drawable.baseline_account_circle_white_18dp));
        listDatos.add(new Medicina_model("Medicina 6","Dosis 6",R.drawable.baseline_account_circle_white_18dp));
        listDatos.add(new Medicina_model("Medicina 7","Dosis 7",R.drawable.baseline_account_circle_white_18dp));
        listDatos.add(new Medicina_model("Medicina 8","Dosis 8",R.drawable.baseline_account_circle_white_18dp));
    }

}
