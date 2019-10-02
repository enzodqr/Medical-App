package com.app.medical.Agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBOpenHelper{

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void saveEvent(final String event, final String time,  final String date, final String month, final String year){

        firestore.collection(DB_Utilities.CALENDAR + auth.getUid())
                .document(event).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if(!snapshot.exists()){

                                Map<String, Object> map = new HashMap<>();
                                map.put("Event_Name", event);
                                map.put("Time", time);
                                map.put("Date", date);
                                map.put("Month", month);
                                map.put("Year", year);

                                firestore.collection(DB_Utilities.CALENDAR + auth.getUid())
                                        .document(event)
                                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Event", "Event Added");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Event", "Event wasn't Added");
                                    }
                                });
                            } else {
                                Log.d("Event", "Event already exist");
                            }
                        } else {
                            Log.d("Event", "Event Failed to create");
                        }
                    }
                });

    }

    public void deleteEvent(String event){
        firestore.collection(DB_Utilities.CALENDAR + auth.getUid())
                .document(event)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Delete Event", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Delete Event", "Error deleting document", e);
                    }
                });
    }

}
