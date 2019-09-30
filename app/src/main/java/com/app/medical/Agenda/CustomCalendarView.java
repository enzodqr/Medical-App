package com.app.medical.Agenda;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
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

import com.app.medical.R;

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
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    AlertDialog alertDialog;
    MyGridAdapter myGridAdapter;

    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    DBOpenHelper dbOpenHelper;


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
                final EditText event_name = add_view.findViewById(R.id.events_id);
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
                                }, hours, minutes, false
                        );
                    }
                });

                final String date = dateFormat.format(dates.get(i));
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



    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void save_event(String event, String time, String date, String month, String year) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.saveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Save", Toast.LENGTH_SHORT).show();
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

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(month_calendar.getTime());
            month_calendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapter = new MyGridAdapter(context,dates,calendar,eventsList);
        gridview.setAdapter(myGridAdapter);

    }

}
