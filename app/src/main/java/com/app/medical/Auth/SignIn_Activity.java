package com.app.medical.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignIn_Activity extends AppCompatActivity {

    Button signIn_btn;

    private static final int RC_SIGN_IN = 7117;
    ArrayList<String> options_list;
    List<AuthUI.IdpConfig> providers;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button sign_out_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        options_list = new ArrayList<>();

        // Array of the signIn providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
                // Add facebook option later
                // new AuthUI.IdpConfig.FacebookBuilder().build()
        );


        // Checks if a user already logged in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
            finish();
        } else {
            show_signIn_options();
        }

    }

    private void show_signIn_options() {
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout.
                Builder(R.layout.activity_sign_in).
                setGoogleButtonId(R.id.Google_btn).
                setEmailButtonId(R.id.Email_btn).
                build();

        startActivityForResult(
                AuthUI.getInstance().
                        createSignInIntentBuilder(). // layout style
                        setAvailableProviders(providers).
                        setIsSmartLockEnabled(false). // Disable smart lock for testing and development
                        setTheme(R.style.MyTheme).
                        setTosAndPrivacyPolicyUrls("https://superapp.example.com/terms-of-service.html",
                                "https://superapp.example.com/privacy-policy.html").
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
                startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
                finish();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_LONG).show();

               // sign_out_btn.setEnabled(true);
            } else {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
