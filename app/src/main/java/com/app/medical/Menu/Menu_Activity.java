package com.app.medical.Menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.medical.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.app.medical.Menu.Adapter.Menu_Adapter;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu_Activity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 7117;
    ArrayList<String> options_list;
    RecyclerView recyclerView;

    List<AuthUI.IdpConfig> providers;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    // Test
    Button sign_out_btn;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Change name

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        this, LinearLayoutManager.VERTICAL, false
                ) // Ignorar error
        );

        options_list = new ArrayList<>();

        set_options();

        Menu_Adapter adapter = new Menu_Adapter(options_list);
        recyclerView.setAdapter(adapter);


        // Test
        sign_out_btn = findViewById(R.id.sign_out_btn);
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(Menu_Activity.this).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                sign_out_btn.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build() //, Add facebook option later
                //new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        showSignInOptions();

    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().
                        createSignInIntentBuilder(). // layout style
                        setAvailableProviders(providers).
                        setIsSmartLockEnabled(false). // Disable smart lock for testing and development
                        setTheme(R.style.MyTheme).
                        setTosAndPrivacyPolicyUrls("https://superapp.example.com/terms-of-service.html",
                                "https://superapp.example.com/privacy-policy.html").
                        build(),
                MY_REQUEST_CODE
        );
    }

    private void set_options(){
        options_list.add("Perfil");
        options_list.add("Agenda");
        options_list.add("Medicina");
        options_list.add("Alertas");
        options_list.add("Farmacias");
        options_list.add("Sugerencias");
        options_list.add("S.O.S");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_LONG).show();

                sign_out_btn.setEnabled(true);
            } else {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
