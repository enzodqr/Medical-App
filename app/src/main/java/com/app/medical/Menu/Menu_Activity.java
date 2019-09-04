package com.app.medical.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.medical.Agenda.Agenda;
import com.app.medical.Profile.Profile_Activity;
import com.app.medical.R;
import com.app.medical.DB_Utilities.DB_Utilities;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu_Activity extends AppCompatActivity  implements View.OnClickListener {

    Button sign_out_btn;
    Button profile_btn;
    Button sos_btn;
    MediaPlayer mediaPlayer; //para la reproducción de sonidos
    Button agenda_btn;

    String user_id;

    // Firebase variables
    private static final int RC_SIGN_IN = 7117;
    List<AuthUI.IdpConfig> providers;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Array of the signIn providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
                // Add facebook option later
                // new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        mediaPlayer = MediaPlayer.create(Menu_Activity.this, R.raw.alarma);

        profile_btn = findViewById(R.id.btn_perfil);
        profile_btn.setOnClickListener(this);
        sign_out_btn = findViewById(R.id.sign_out_btn);
        sign_out_btn.setOnClickListener(this);
        agenda_btn = findViewById(R.id.btn_agenda);
        agenda_btn.setOnClickListener(this);
        sos_btn = findViewById(R.id.btn_sos);
        sos_btn.setOnClickListener(this);


        // Checks if a user already logged in
        if (auth.getCurrentUser() != null) {
            user_id = user.getUid();
            Toast.makeText(Menu_Activity.this, "Contacto: " + user_id,
                    Toast.LENGTH_SHORT).show();
        } else {
            show_signIn_options();
        }

    }

    // Menu button's functionality
    // Ordered by menu display order
    @Override
    public void onClick(View view) {

        // Gets the button id
        int id = view.getId();

        if(id == R.id.btn_perfil){
            go_to_profile();
        } else if(id == R.id.btn_agenda){
            startActivity(new Intent(getApplicationContext(), Agenda.class));
        } else if (id == R.id.btn_sos){
            media_player();
        } else if(id == R.id.sign_out_btn) {
            sign_out_db();
        }
    }

    private void show_signIn_options() {
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout.
                Builder(R.layout.activity_sign_in).
                setGoogleButtonId(R.id.Google_btn).
                setEmailButtonId(R.id.Email_btn).
                setPhoneButtonId(R.id.Phone_btn).
                build();

        startActivityForResult(
                AuthUI.getInstance().
                        createSignInIntentBuilder(). // layout style
                        setAvailableProviders(providers).
                        setIsSmartLockEnabled(false). // Disable smart lock for testing and development
                        setTheme(R.style.Firebase_theme).
                        setAuthMethodPickerLayout(customLayout).
                        build(),
                RC_SIGN_IN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                add_user_db();
            } else {
                Toast.makeText(this, ""+response.getError().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    // Checks if the user exists in the db
    // if not the user is added else it does nothing
    private void add_user_db(){

        // Each document name is equal to the user uid

        // Tells which document whe want to check
        DocumentReference documentReference = firestore.collection(DB_Utilities.USERS).
                document(user.getUid());

        // Asks if the document exist
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Toast.makeText(Menu_Activity.this, "Document exists!",
                                Toast.LENGTH_LONG).show();
                    } else {


                        Map<String, Object> map = new HashMap<>();
                        map.put(DB_Utilities.USER_NAME, user.getDisplayName());
                        map.put(DB_Utilities.USER_ID, 0);
                        map.put(DB_Utilities.USER_AGE, 0);
                        map.put(DB_Utilities.USER_GENDER, "");
                        map.put(DB_Utilities.USER_PHONE, 0);
                        map.put(DB_Utilities.USER_BLOOD_TYPE, "");
                        map.put(DB_Utilities.USER_ADDRESS,"");
                        map.put(DB_Utilities.USER_EMERGENCY_CONTACT, "");
                        map.put(DB_Utilities.USER_NATIONALITY, "");


                        firestore.collection(DB_Utilities.USERS).document(user.getUid()).set(map).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Menu_Activity.this, "User addedd!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Menu_Activity.this, "Error!" + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(Menu_Activity.this, "Failed: !" + task.getException(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    private void go_to_profile(){
        Intent intent = new Intent(getApplicationContext(), Profile_Activity.class);
        intent.putExtra("uid", user_id);
        startActivity(intent);
        finish();
    }

    // Plays the alarm of the sos button
    private void media_player(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
            Toast.makeText(Menu_Activity.this, "Mensaje Enviado a 'Contacto'", Toast.LENGTH_SHORT).show();
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

}
