package com.app.medical.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;

public class SignIn_Activity extends AppCompatActivity {

    Button signIn_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signIn_btn = findViewById(R.id.signIn_btn);
        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
            }
        });
    }
}
