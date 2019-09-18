package com.app.medical.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class Profile_Activity extends AppCompatActivity {

    ArrayList<Profile_Model> profile_list;
    RecyclerView recyclerView;

    ImageButton back_btn;
    //ImageButton edit_btn;

    String user_id;

    //para el modal de editar
    Dialog pop_up;

    User_Model user;

    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pop_up = new Dialog(this);


        Intent intent = getIntent();
        user_id = intent.getStringExtra("uid");

        documentReference = FirebaseFirestore.getInstance().document(DB_Utilities.USERS+"/"+user_id);

        profile_list = new ArrayList<>();

        recyclerView = findViewById(R.id.Profile_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.
               VERTICAL, false));

        get_user_data();

        //Profile_Adapter adapter = new Profile_Adapter(profile_list);
        //recyclerView.setAdapter(adapter);

        back_btn = findViewById(R.id.profile_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_to_menu();
            }
        });




        // ------------------------
        /*edit_btn = findViewById(R.id.profile_edit);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_modal(view);
            }
        });*/
        // ------------------------
    }
    private void Add_Data() {
        profile_list.add(new Profile_Model("Email:", "test@test.com"));
        profile_list.add(new Profile_Model("Teléfono:", "+506 88213221"));
        profile_list.add(new Profile_Model("Ubicación:", "Alajuela Centro"));
        profile_list.add(new Profile_Model("Tipo de Sangre:", "A+"));
        profile_list.add(new Profile_Model("Alergias", "Ninguna"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return_to_menu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void return_to_menu(){
        startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
        finish();
    }

    // ------------------------
    /*public void show_modal(View v){
        TextView cerrar;
        ImageButton btn_cancel;

        pop_up.setContentView(R.layout.edit_popup);
        cerrar = (TextView) pop_up.findViewById(R.id.txt_cerrar_popup);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up.dismiss();
            }
        });
        pop_up.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up.show();

        btn_cancel = (ImageButton) pop_up.findViewById(R.id.btn_cancelEdit);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up.dismiss();
            }
        });

    }*/
    // ------------------------

    public void get_user_data(){
        //Source source = Source.CACHE;

        /*documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Log.d("Data user data", "Data: " + documentSnapshot.getString("name"));
                    String nombre = documentSnapshot.getString("name");
                    profile_list.add(new Profile_Model("Email:", nombre));
                }
            }
        });*/

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Log.d("Data user data", "Data: " + documentSnapshot.getData());
                    String nombre = documentSnapshot.getString("name");
                    profile_list.add(new Profile_Model("Email:", nombre));
                    Profile_Adapter adapter = new Profile_Adapter(profile_list);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
}