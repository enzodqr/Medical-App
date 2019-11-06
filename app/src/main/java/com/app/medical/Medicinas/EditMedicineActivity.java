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

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class EditMedicineActivity extends AppCompatActivity {

    String nombre_medicina;
    Intent intent;

    EditText medicina, dosis, periodo, uso, fecha;
    Spinner presentacion;
    Button guardar, cancelar;
    ImageButton regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        intent = getIntent();
        nombre_medicina = intent.getExtras().getString("Medicina");
        Log.d("Medicina", nombre_medicina);

        medicina = findViewById(R.id.edit_med_medicina);
        dosis = findViewById(R.id.edit_med_dosis);
        periodo = findViewById(R.id.edit_med_periodo);
        uso = findViewById(R.id.edit_med_uso);
        presentacion =  findViewById(R.id.edit_med_presentacion);
        fecha = findViewById(R.id.edit_med_fecha);
        guardar = findViewById(R.id.btn_edit_med_guardar);
        cancelar = findViewById(R.id.btn_edit_med_cancelar);
        regresar = findViewById(R.id.edit_med_back);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
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

        get_data();
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
        presentacion.setSelection(getIndex(presentacion, list.get(4)));
        fecha.setText(list.get(5));
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    private void update(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.
                collection(DB_Utilities.MEDICINE + auth.getUid()).document(nombre_medicina);

        documentReference.update(
                DB_Utilities.MED_MEDICINA, medicina.getText().toString(),
                DB_Utilities.MED_DOSIS, dosis.getText().toString(),
                DB_Utilities.MED_PERIODO, periodo.getText().toString(),
                DB_Utilities.MED_USO, uso.getText().toString(),
                DB_Utilities.MED_PRESENTACION, presentacion.getSelectedItem().toString(),
                DB_Utilities.MED_FECHA, fecha.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Message", "Success");
                } else {
                    Log.d("Message", "Failed");
                }
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
