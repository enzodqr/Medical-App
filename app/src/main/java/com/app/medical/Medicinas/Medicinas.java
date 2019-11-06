package com.app.medical.Medicinas;

import android.content.Intent;
import android.os.Bundle;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.Menu.Menu_Activity;
import com.app.medical.Profile.Profile_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.medical.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class Medicinas extends AppCompatActivity {

    ArrayList<Medicina_model> listDatos;
    RecyclerView recycler;
    ImageButton back_btn;

    TextView medicine_msg;

    Medicina_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicinas);

        listDatos = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.recycler_medicinas);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        back_btn = findViewById(R.id.medicine_back);

        medicine_msg =findViewById(R.id.medicine_msg);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_menu();
            }
        });

        check_data();

        get_data();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_medicine();
            }
        });
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


    public void show_msg(boolean flag){

        if(flag){
            medicine_msg.setVisibility(View.VISIBLE);
        } else {
            medicine_msg.setVisibility(View.GONE);
        }

    }

    public void add_medicine(){
        Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class);
        startActivity(intent);
        finish();
    }

    private void check_data(){

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firestore.
                collection(DB_Utilities.MEDICINE + auth.getUid());

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size() != 0){

                    Log.d("Medicine", "Collection exists!");
                    show_msg(false);
                } else {
                    show_msg(true);
                }
            }
        });

    }

    private void get_data(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firestore.
                collection(DB_Utilities.MEDICINE + auth.getUid());

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        listDatos.add(new Medicina_model(document.get("medicina").toString(),document.get("dosis").toString(),R.drawable.baseline_account_circle_white_18dp));

                    }
                    adapter = new Medicina_Adapter(listDatos);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MedicineInfo.class);
                            intent.putExtra("Medicina", listDatos.get(recycler.getChildAdapterPosition(view)).getNombre());
                            startActivity(intent);
                            finish();
                        }
                    });
                    recycler.setAdapter(adapter);
                    Log.d("Medicine", listDatos.toString());
                } else {
                    Log.d("Medicine", "Error getting documents: ", task.getException());
                }
            }
        });
    }


}
