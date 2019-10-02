package com.app.medical.Agenda;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.app.medical.Agenda.Fragments.Calendar_fragment;
import com.app.medical.Agenda.Fragments.Events_fragment;
import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.Menu.Menu_Activity;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Agenda extends AppCompatActivity
        implements Calendar_fragment.OnFragmentInteractionListener,
            Events_fragment.OnFragmentInteractionListener {

    //CustomCalendarView customCalendarView;


    Calendar_fragment calendar_fragment;
    Events_fragment events_fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        //customCalendarView = findViewById(R.id.custom_calendar_view);

        calendar_fragment = new Calendar_fragment();
        events_fragment = new Events_fragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragments_agenda, calendar_fragment).commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onClick(View view){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (view.getId()){
            case R.id.calendar_btn:
                fragmentTransaction.replace(R.id.fragments_agenda, calendar_fragment);
                break;
            case  R.id.events_btn:
                fragmentTransaction.replace(R.id.fragments_agenda, events_fragment);
                break;
        }

        fragmentTransaction.commit();
    }
}

