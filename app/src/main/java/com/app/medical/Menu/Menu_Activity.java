package com.app.medical.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.medical.R;

import java.util.Arrays;
import java.util.List;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu_Activity extends AppCompatActivity {

    Button sign_out_btn;

    // Firebase variables
    private static final int RC_SIGN_IN = 7117;
    List<AuthUI.IdpConfig> providers;
    FirebaseAuth auth = FirebaseAuth.getInstance();


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


        // Checks if a user already logged in
        if (auth.getCurrentUser() != null) {
            Toast.makeText(Menu_Activity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
        } else {
            show_signIn_options();
        }


        // Sign out button, move to the profile interfase
        sign_out_btn = findViewById(R.id.sign_out_btn);
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(Menu_Activity.this).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                show_signIn_options();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
                //startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
                //finish();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_LONG).show();

                // sign_out_btn.setEnabled(true);
            } else {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
