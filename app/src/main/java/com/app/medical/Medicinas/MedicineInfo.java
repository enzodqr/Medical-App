package com.app.medical.Medicinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.medical.DB_Utilities.DB_Utilities;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MedicineInfo extends AppCompatActivity {

    ImageButton back_btn;
    String nombre_medicina;
    Intent intent;

    TextView medicina, dosis, periodo, uso, fecha, presentacion;
    Button editar, borrar;
    ImageButton regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);

        medicina = findViewById(R.id.info_med_titulo);
        dosis = findViewById(R.id.info_med_dosis);
        periodo = findViewById(R.id.info_med_periodo);
        uso = findViewById(R.id.info_med_uso);
        presentacion =  findViewById(R.id.info_med_presentacion);
        fecha = findViewById(R.id.info_med_fecha);
        editar = findViewById(R.id.info_med_editar);
        borrar = findViewById(R.id.info_med_borrar);

        regresar = findViewById(R.id.info_med_back);

        intent = getIntent();
        nombre_medicina = intent.getExtras().getString("Medicina");
        Log.d("Medicina", nombre_medicina);

        get_data();

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_medicine();
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_medicine();
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_med();

            }
        });

    }

    private void get_data(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.
                collection(DB_Utilities.MEDICINE + auth.getUid()).document(nombre_medicina);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    List<String> list = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                    list.add(document.get(DB_Utilities.MED_MEDICINA).toString());
                    list.add(document.get(DB_Utilities.MED_DOSIS).toString());
                    list.add(document.get(DB_Utilities.MED_PERIODO).toString());
                    list.add(document.get(DB_Utilities.MED_USO).toString());
                    list.add(document.get(DB_Utilities.MED_PRESENTACION).toString());
                    list.add(document.get(DB_Utilities.MED_FECHA).toString());

                    set_text(list);

                    Log.d("Medicina", "Lista: "+list.toString());

                } else {
                    Log.d("Medicina", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void set_text(List<String> list){
        medicina.setText(list.get(0));
        dosis.setText(list.get(1));
        periodo.setText(list.get(2));
        uso.setText(list.get(3));
        presentacion.setText(list.get(4));
        fecha.setText(list.get(5));
    }

    private void edit_medicine(){
        Intent intent = new Intent(getApplicationContext(), EditMedicineActivity.class);
        intent.putExtra("Medicina", nombre_medicina);
        startActivity(intent);
        finish();
    }

    private void delete_medicine(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.
                collection(DB_Utilities.MEDICINE + auth.getUid()).document(nombre_medicina);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Medicina", "DocumentSnapshot successfully deleted!");
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Medicina", "DocumentSnapshot successfully deleted!");
                    }
                });
    }

    private void cancel_med(){
        Intent intent = new Intent(getApplicationContext(), Medicinas.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            cancel_med();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
