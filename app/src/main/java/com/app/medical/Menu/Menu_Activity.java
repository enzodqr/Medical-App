package com.app.medical.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.app.medical.R;

import java.util.ArrayList;

import com.app.medical.Menu.Adapter.Menu_Adapter;

public class Menu_Activity extends AppCompatActivity {

    ArrayList<String> options_list;
    RecyclerView recyclerView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Change name

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, LinearLayoutManager.VERTICAL, false
                ) // Ignorar error
        );

        options_list = new ArrayList<>();

        set_options();

        Menu_Adapter adapter = new Menu_Adapter(options_list);
        recyclerView.setAdapter(adapter);

    }

    private void set_options(){
        options_list.add("Perfil");
        options_list.add("Agenda");
        options_list.add("Medicina");
        options_list.add("Alertas");
        options_list.add("Farmacias");
        options_list.add("Sugerencias");
        options_list.add("S.O.S");
    }
}
