package com.app.medical.Medicinas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.app.medical.R;

public class MedicineInfo extends AppCompatActivity {

    ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);

        back_btn = findViewById(R.id.info_medicine_back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_medicine();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            go_to_medicine();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void go_to_medicine(){
        Intent intent = new Intent(getApplicationContext(), Medicinas.class);
        startActivity(intent);
        finish();
    }
}
