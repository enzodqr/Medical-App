package com.app.medical.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.medical.R;

import java.util.ArrayList;

public class Profile_Activity extends AppCompatActivity {

    ArrayList<Profile_Model> profile_list;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_list = new ArrayList<>();

        recyclerView = findViewById(R.id.Profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        Add_Data();


        Profile_Adapter adapter = new Profile_Adapter(profile_list);
        recyclerView.setAdapter(adapter);
    }

    private void Add_Data() {
        profile_list.add(new Profile_Model("Email:", "test@test.com"));
        profile_list.add(new Profile_Model("Teléfono:", "+506 88213221"));
        profile_list.add(new Profile_Model("Ubicación:", "Alajuela Centro"));
        profile_list.add(new Profile_Model("Tipo de Sangre:", "A+"));
        profile_list.add(new Profile_Model("Alergias", "Ninguna"));
    }
}
