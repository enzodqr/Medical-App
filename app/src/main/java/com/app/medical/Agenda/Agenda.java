package com.app.medical.Agenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;

public class Agenda extends AppCompatActivity{

    CustomCalendarView customCalendarView;
    ImageButton home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        home = (ImageButton) findViewById(R.id.calendar_back);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Menu_Activity.class));
            }
        });

        customCalendarView = findViewById(R.id.custom_calendar_view);
    }
}