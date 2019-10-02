package com.app.medical.Agenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.medical.DB_Utilities.DB_Utilities;
import com.app.medical.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

    ImageButton next, prev; // para paginación de meses
    TextView currentDate; //fecha actual
    GridView gridview; //vista del calendario
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    /*formatos de las fechas*/
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy",Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    AlertDialog alertDialog;
    MyGridAdapter myGridAdapter;

    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    DBOpenHelper dbOpenHelper;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeLayout();
        setUpCalendar();

        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                setUpCalendar();
            }
        });

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, +1);
                setUpCalendar();
            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View add_view = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.add_newevent_layout, null);
                final EditText event_name = add_view.findViewById(R.id.event_name);
                final TextView event_time = add_view.findViewById(R.id.event_time);
                ImageButton set_time = add_view.findViewById(R.id.set_event_time);
                Button add_event = add_view.findViewById(R.id.add_event);
                set_time.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        final int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        final int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                add_view.getContext(), R.style.Theme_AppCompat_Dialog,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.HOUR_OF_DAY, i);
                                        c.set(Calendar.MINUTE, i1);
                                        c.setTimeZone(TimeZone.getDefault());
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                                "K:mm a", Locale.ENGLISH
                                        );
                                        String event_Time = simpleDateFormat.format(c.getTime());
                                        event_time.setText(event_Time);
                                    }
                                }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });

                final String date = eventDateFormat.format(dates.get(i));
                final String month = monthFormat.format(dates.get(i));
                final String year = yearFormat.format(dates.get(i));

                add_event.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        save_event(event_name.getText().toString(), event_time.getText().toString(),
                        date, month, year);
                        setUpCalendar();
                        alertDialog.dismiss();
                    }
                });

                builder.setView(add_view);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                String date = eventDateFormat.format(dates.get(i));
                final ArrayList<Events> eventsArrayList = new ArrayList<>();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View showview = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
                final RecyclerView recyclerView = showview.findViewById(R.id.events_rv);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showview.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

                firestore.collection(DB_Utilities.CALENDAR + auth.getUid())
                        .whereEqualTo("Date", date)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("Read event", document.getId() + " => " + document.getData());
                                        Events events = new Events(
                                                document.get("Event_Name").toString(),
                                                document.get("Time").toString(),
                                                document.get("Date").toString(),
                                                document.get("Month").toString(),
                                                document.get("Year").toString()
                                        );

                                        eventsArrayList.add(events);
                                        Log.d("Read event 1", eventsArrayList.toString());
                                        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showview.getContext(),
                                                eventsArrayList);
                                        recyclerView.setAdapter(eventRecyclerAdapter);
                                        eventRecyclerAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Log.d("Read event", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                builder.setView(showview);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        setUpCalendar();
                    }
                });
;
                return true;
            }
        });



    }


    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void save_event(String event, String time, String date, String month, String year) {
        dbOpenHelper = new DBOpenHelper();
        dbOpenHelper.saveEvent(event, time, date, month, year);
        Toast.makeText(context, "Evento Guardado", Toast.LENGTH_SHORT).show();
    }

    private void initializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        next = view.findViewById(R.id.next_btn);
        prev = view.findViewById(R.id.previous_btn);
        currentDate = findViewById(R.id.txt_curDate);
        gridview = findViewById(R.id.gridview);
    }

    private void setUpCalendar(){
        String current_date = dateFormat.format(calendar.getTime());
        currentDate.setText(current_date);
        dates.clear();
        Calendar month_calendar = (Calendar)calendar.clone();
        month_calendar.set(Calendar.DAY_OF_MONTH, 1);
        int first_day_of_month = month_calendar.get(Calendar.DAY_OF_WEEK)-1;
        month_calendar.add(Calendar.DAY_OF_MONTH, -first_day_of_month);
        //CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(month_calendar.getTime());
            month_calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        eventsList.clear();

        firestore.collection(DB_Utilities.CALENDAR + auth.getUid())
                .whereEqualTo("Month", monthFormat.format(calendar.getTime()))
                .whereEqualTo("Year", yearFormat.format(calendar.getTime()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Compose", document.getId() + " => " + document.getData());
                                Events events = new Events(
                                        document.get("Event_Name").toString(),
                                        document.get("Time").toString(),
                                        document.get("Date").toString(),
                                        document.get("Month").toString(),
                                        document.get("Year").toString()
                                );

                                eventsList.add(events);
                                myGridAdapter = new MyGridAdapter(context,dates,calendar,eventsList);
                                gridview.setAdapter(myGridAdapter);
                            }
                        } else {
                            Log.d("Compose", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
