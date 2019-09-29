package com.app.medical.Agenda;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.medical.R;

public class Agenda extends AppCompatActivity{

    CustomCalendarView customCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        customCalendarView = findViewById(R.id.custom_calendar_view);
    }
}