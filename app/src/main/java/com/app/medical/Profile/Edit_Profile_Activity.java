package com.app.medical.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Edit_Profile_Activity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    EditText name_txt, id_txt, age_txt, phone_txt, emergency_contact_txt, address_txt, nationality_txt;
    Spinner spinner_gender;
    Spinner spinner_blood_type;
    ArrayAdapter<CharSequence> arrayAdapter_gender;
    ArrayAdapter<CharSequence> arrayAdapter_blood_type;
    Button save_changes_btn;
    Button cancel_changes_btn;

    User_Model user_model = new User_Model();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        user_model = intent.getParcelableExtra("User_Object");

        Log.d("User", user_model.toString());

        name_txt = findViewById(R.id.edit_nombre);
        id_txt = findViewById(R.id.edit_id);
        age_txt = findViewById(R.id.edit_edad);
        phone_txt = findViewById(R.id.edit_telefono);
        emergency_contact_txt = findViewById(R.id.edit_contacto_emergencia);
        address_txt = findViewById(R.id.edit_direccion);
        nationality_txt = findViewById(R.id.edit_nacionalidad);

        spinner_gender = findViewById(R.id.edit_genero);
        spinner_blood_type = findViewById(R.id.edit_tipo_sangre);

        save_changes_btn = findViewById(R.id.edit_save);
        cancel_changes_btn = findViewById(R.id.edit_cancel);

        arrayAdapter_gender = ArrayAdapter.createFromResource(this, R.array.generos,
                android.R.layout.simple_spinner_item);
        arrayAdapter_blood_type = ArrayAdapter.createFromResource(this, R.array.tipos_sangres,
                android.R.layout.simple_spinner_item);

        arrayAdapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter_blood_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_gender.setAdapter(arrayAdapter_gender);
        spinner_blood_type.setAdapter(arrayAdapter_blood_type);

        spinner_gender.setOnItemSelectedListener(this);
        spinner_blood_type.setOnItemSelectedListener(this);

        fill_form();

        save_changes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_db();
                return_to_profile();
            }
        });

        cancel_changes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_to_profile();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void fill_form(){
        name_txt.setText(user_model.getName());
        id_txt.setText(String.valueOf(user_model.getId()));
        age_txt.setText(String.valueOf(user_model.getAge()));
        phone_txt.setText(String.valueOf(user_model.getPhone()));
        emergency_contact_txt.setText(String.valueOf(user_model.getEmergency_contact()));
        address_txt.setText(user_model.getAddress());
        nationality_txt.setText(user_model.getNationality());
        spinner_blood_type.setSelection(
                arrayAdapter_blood_type.getPosition(user_model.getBlood_type())
        );
        spinner_gender.setSelection(
                arrayAdapter_gender.getPosition(user_model.getGender())
        );
    }

    public void update_db(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Log.d("Message", auth.getUid());

        DocumentReference reference = firestore
                .document(DB_Utilities.USERS+"/"+auth.getUid());

        reference.update(
                DB_Utilities.USER_ADDRESS, address_txt.getText().toString(),
                DB_Utilities.USER_AGE, Integer.parseInt(age_txt.getText().toString()),
                DB_Utilities.USER_BLOOD_TYPE, spinner_blood_type.getSelectedItem().toString(),
                DB_Utilities.USER_EMERGENCY_CONTACT, Integer.parseInt(emergency_contact_txt.getText().toString()),
                DB_Utilities.USER_GENDER, spinner_gender.getSelectedItem().toString(),
                DB_Utilities.USER_ID, Integer.parseInt(id_txt.getText().toString()),
                DB_Utilities.USER_NAME, name_txt.getText().toString(),
                DB_Utilities.USER_NATIONALITY, nationality_txt.getText().toString(),
                DB_Utilities.USER_PHONE, Integer.parseInt(phone_txt.getText().toString())
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

    /*
     *   Back functionality of androids interface's back btn
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return_to_profile();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     *   Return to profile function,
     *   handles the startActivity and the intent
     * */
    private void return_to_profile(){
        startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
        finish();
    }
}
