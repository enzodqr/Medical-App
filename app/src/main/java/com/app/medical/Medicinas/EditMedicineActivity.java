package com.app.medical.Medicinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EditMedicineActivity extends AppCompatActivity {

    EditText medicina, dosis, periodo, uso, fecha;
    Spinner presentacion;
    Button guardar, cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        medicina = findViewById(R.id.edit_med_medicina);
        dosis = findViewById(R.id.edit_med_dosis);
        periodo = findViewById(R.id.edit_med_periodo);
        uso = findViewById(R.id.edit_med_uso);
        presentacion =  findViewById(R.id.edit_med_presentacion);
        fecha = findViewById(R.id.edit_med_fecha);
        guardar = findViewById(R.id.btn_edit_med_guardar);
        cancelar = findViewById(R.id.btn_edit_med_cancelar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save_med();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cancel_med();
            }
        });
    }

    private void print_data(){

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
                        list.add(document.get(DB_Utilities.MED_MEDICINA).toString());
                        list.add(document.get(DB_Utilities.MED_DOSIS).toString());
                        list.add(document.get(DB_Utilities.MED_PERIODO).toString());
                        list.add(document.get(DB_Utilities.MED_USO).toString());
                        list.add(document.get(DB_Utilities.MED_PRESENTACION).toString());
                        list.add(document.get(DB_Utilities.MED_FECHA).toString());
                    }

                } else {
                    Log.d("Medicine", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
