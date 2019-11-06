package com.app.medical.Medicinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddMedicineActivity extends AppCompatActivity {

    EditText medicina, dosis, periodo, uso, fecha;
    Spinner presentacion;
    Button guardar, cancelar;
    ImageButton regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        medicina = findViewById(R.id.add_med_medicina);
        dosis = findViewById(R.id.add_med_dosis);
        periodo = findViewById(R.id.add_med_periodo);
        uso = findViewById(R.id.add_med_uso);
        presentacion =  findViewById(R.id.add_med_presentacion);
        fecha = findViewById(R.id.add_med_fecha);
        guardar = findViewById(R.id.btn_add_med_guardar);
        cancelar = findViewById(R.id.btn_add_med_cancelar);
        regresar = findViewById(R.id.add_medicine_back);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_med();
                Toast.makeText(AddMedicineActivity.this, "Agregado Correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddMedicineActivity.this, Medicinas.class));
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_med();

            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_med();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            cancel_med();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void save_med(){

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Collection and document that is going to be used
        DocumentReference documentReference = firestore.collection(DB_Utilities.MEDICINE + auth.getUid()).
                document(medicina.getText().toString());

        // Asks if the document exist
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Log.i("Medicina","Existe medicamento!");

                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put(DB_Utilities.MED_MEDICINA, medicina.getText().toString());
                        map.put(DB_Utilities.MED_DOSIS, dosis.getText().toString());
                        map.put(DB_Utilities.MED_PERIODO, periodo.getText().toString());
                        map.put(DB_Utilities.MED_USO, uso.getText().toString());
                        map.put(DB_Utilities.MED_PRESENTACION, presentacion.getSelectedItem().toString());
                        map.put(DB_Utilities.MED_FECHA, fecha.getText().toString());

                        firestore.collection(DB_Utilities.MEDICINE + auth.getUid()).
                                document(medicina.getText().toString()).set(map).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("Medicina","Medicina agregada!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Medicina", e.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("Medicina", task.getException().getMessage());
                }
            }
        });
    }

    private void cancel_med(){
        Intent intent = new Intent(getApplicationContext(), Medicinas.class);
        startActivity(intent);
        finish();
    }

}
