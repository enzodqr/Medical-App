package com.app.medical.Medicinas;

import android.content.Intent;
import android.os.Bundle;

import com.app.medical.Menu.Menu_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.medical.R;

import java.util.ArrayList;

public class Medicinas extends AppCompatActivity {

    ArrayList<Medicina_model> listDatos;
    RecyclerView recycler;

    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinas);

        listDatos = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.recycler_medicinas);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        back_btn = findViewById(R.id.medicine_back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_menu();
            }
        });

        rellenarLista();

        Medicina_Adapter adapter = new Medicina_Adapter(listDatos);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_medicine();
                Toast.makeText(Medicinas.this, "Presionado: "+listDatos.get(recycler.getChildAdapterPosition(view)).getNombre()+" ", Toast.LENGTH_SHORT).show();
            }
        });

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
        listDatos.add(new Medicina_model("Inyección","Dosis 1",R.drawable.inyeccion));
        listDatos.add(new Medicina_model("Jarabe","Dosis 2",R.drawable.jarabe));
        listDatos.add(new Medicina_model("Pastillas","Dosis 3",R.drawable.pills));
        listDatos.add(new Medicina_model("Inyección","Dosis 1",R.drawable.inyeccion));
        listDatos.add(new Medicina_model("Jarabe","Dosis 2",R.drawable.jarabe));
        listDatos.add(new Medicina_model("Pastillas","Dosis 3",R.drawable.pills));
        listDatos.add(new Medicina_model("Inyección","Dosis 1",R.drawable.inyeccion));
        listDatos.add(new Medicina_model("Jarabe","Dosis 2",R.drawable.jarabe));
        listDatos.add(new Medicina_model("Pastillas","Dosis 3",R.drawable.pills));
    }

    private void go_to_medicine(){
        Intent intent = new Intent(getApplicationContext(), MedicineInfo.class);
        startActivity(intent);
        finish();
    }

    private void go_to_menu(){
        Intent intent = new Intent(getApplicationContext(), Menu_Activity.class);
        startActivity(intent);
        finish();
    }

    /*
     *   Back functionality of androids interface's back btn
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            go_to_menu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
