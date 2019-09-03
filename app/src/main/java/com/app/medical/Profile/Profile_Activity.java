package com.app.medical.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;

import java.util.ArrayList;

public class Profile_Activity extends AppCompatActivity {

    ArrayList<Profile_Model> profile_list;
    RecyclerView recyclerView;

    ImageButton back_btn;

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

        back_btn = findViewById(R.id.profile_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_to_menu();
            }
        });
    }
    private void Add_Data() {
        profile_list.add(new Profile_Model("Email:", "test@test.com"));
        profile_list.add(new Profile_Model("Teléfono:", "+506 88213221"));
        profile_list.add(new Profile_Model("Ubicación:", "Alajuela Centro"));
        profile_list.add(new Profile_Model("Tipo de Sangre:", "A+"));
        profile_list.add(new Profile_Model("Alergias", "Ninguna"));
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return_to_menu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    private void return_to_menu(){
        startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
        finish();
    }
}