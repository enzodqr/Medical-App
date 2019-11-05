package com.app.medical.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.medical.Agenda.Agenda;
import com.app.medical.Alertas.NuevaAlarma;
import com.app.medical.Medicinas.Medicinas;
import com.app.medical.Profile.Profile_Activity;
import com.app.medical.Profile.Profile_Model;
import com.app.medical.Profile.User_Model;
import com.app.medical.R;
import com.app.medical.DB_Utilities.DB_Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu_Activity extends AppCompatActivity  implements View.OnClickListener {

   /* FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();*/
  //  User_Model user_model = new User_Model();

    ArrayList<Profile_Model> profile_list;
    User_Model user;

    /* Display the menu for all the app's main functions */


    /* Menu's buttons */
    Button profile_btn;
    Button agenda_btn;
    Button sos_btn;
    Button sign_out_btn;
    Button medicina_btn;
    Button alertas_btn;

    /* Alarm (Me cago en lo ruidoso que es) */
    MediaPlayer mediaPlayer; //para la reproducción de sonidos

    /* Firebase variables*/
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    /* Providers and signIn key */
    private static final int RC_SIGN_IN = 7117;
    List<AuthUI.IdpConfig> providers;

    /* User data variables */
    String user_uid;
    String user_name;
    String emergency;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        profile_btn = findViewById(R.id.btn_perfil);
        sign_out_btn = findViewById(R.id.sign_out_btn);
        agenda_btn = findViewById(R.id.btn_agenda);
        medicina_btn = findViewById(R.id.btn_medicinas);
        alertas_btn = findViewById(R.id.btn_alertas);

        sos_btn = findViewById(R.id.btn_sos);



        agenda_btn.setOnClickListener(this);
        sign_out_btn.setOnClickListener(this);
        profile_btn.setOnClickListener(this);
        sos_btn.setOnClickListener(this);
        medicina_btn.setOnClickListener(this);
        alertas_btn.setOnClickListener(this);


        mediaPlayer = MediaPlayer.create(Menu_Activity.this, R.raw.alarma);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        // Array of the signIn providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
                // Add facebook option later
                // new AuthUI.IdpConfig.FacebookBuilder().build()
        );


        // Checks if a user already logged in
        if (auth.getCurrentUser() == null) {
            show_signIn_options();
        }


    }



    @Override
    public void onClick(View view) {

        /* Menu button's functionality, ordered by menu display order */

        int id = view.getId();

        if(id == R.id.btn_perfil){
            go_to_profile();
        } else if(id == R.id.btn_agenda){
            startActivity(new Intent(getApplicationContext(), Agenda.class));
        } else if(id == R.id.btn_medicinas){
            startActivity(new Intent(getApplicationContext(), Medicinas.class));
        } else if(id == R.id.btn_alertas) {
            startActivity(new Intent(getApplicationContext(), NuevaAlarma.class));
        } else if (id == R.id.btn_sos){

            if(ActivityCompat.checkSelfPermission(Menu_Activity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(Menu_Activity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){ActivityCompat.requestPermissions(Menu_Activity.this, new String[]
                    {
                            Manifest.permission.SEND_SMS,}, 1000);

            }else{

            }

            media_player();

        } else if(id == R.id.sign_out_btn) {
            sign_out_db();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                add_user_db();
                Log.i("SignIn Success","Ingreso Exitoso");

            } else {
                Log.e("SignIn Error", response.getError().getMessage());
            }
        }
    }


    private void show_signIn_options() {
        /* LogIn layout settings */
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout.
                Builder(R.layout.activity_sign_in).
                setGoogleButtonId(R.id.Google_btn).
                setEmailButtonId(R.id.Email_btn).
                setPhoneButtonId(R.id.Phone_btn).
                build();

        /* Login module settings */
        startActivityForResult(
                AuthUI.getInstance().
                        createSignInIntentBuilder().
                        setAvailableProviders(providers).
                        setIsSmartLockEnabled(false). // Disable smart lock for testing and development
                        setTheme(R.style.Firebase_theme).
                        setAuthMethodPickerLayout(customLayout).
                        build(),
                RC_SIGN_IN
        );
    }




    /* Checks if the user exists in the db, if not the user is added else it does nothing*/
    private void add_user_db(){

        user_uid = auth.getUid();
        user_name = auth.getCurrentUser().getDisplayName();

        // Collection and document that is going to be used
        DocumentReference documentReference = firestore.collection(DB_Utilities.USERS).
                document(user_uid);

        // Asks if the document exist
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Log.i("Test","Existe usuario!");

                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put(DB_Utilities.USER_NAME, user_name);
                        map.put(DB_Utilities.USER_ID, 0);
                        map.put(DB_Utilities.USER_AGE, 0);
                        map.put(DB_Utilities.USER_GENDER, "");
                        map.put(DB_Utilities.USER_PHONE, 0);
                        map.put(DB_Utilities.USER_BLOOD_TYPE, "");
                        map.put(DB_Utilities.USER_ADDRESS,"");
                        map.put(DB_Utilities.USER_EMERGENCY_CONTACT, 0);
                        map.put(DB_Utilities.USER_NATIONALITY, "");

                        firestore.collection(DB_Utilities.USERS).document(user_uid).set(map).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("Test","Usuario agregádo!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("SignIn Error", e.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("SignIn Error", task.getException().getMessage());
                }
            }
        });
    }


    private void go_to_profile(){
        Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
        startActivity(intent);
        finish();
    }

    // Plays the alarm of the sos button
    private void media_player(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
            emergency_contact();
           // Toast.makeText(Menu_Activity.this, "Mensaje Enviado a 'Contacto'", Toast.LENGTH_SHORT).show();
        }
    }

    private void sign_out_db(){
        AuthUI.getInstance().signOut(Menu_Activity.this).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        show_signIn_options();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Menu_Activity.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* sos
    * id =btn_sos */

    private void enviarMensaje(String numero, String mensaje){
        try{
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero, null, mensaje, null, null);
            Toast.makeText(getApplicationContext(), "Mensaje enviado", Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Mensaje no enviado", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void emergency_contact(){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.
                collection(DB_Utilities.USERS).document(auth.getUid());

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot =task.getResult();
                    emergency = snapshot.get(DB_Utilities.USER_EMERGENCY_CONTACT).toString();
                    enviarMensaje(emergency, "Emergencia!");
                }
            }
        });
    }
}
