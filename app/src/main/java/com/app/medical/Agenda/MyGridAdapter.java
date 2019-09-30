package com.app.medical.Agenda;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.medical.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date month_date = dates.get(position);
        Calendar date_calendar = Calendar.getInstance();
        date_calendar.setTime(month_date);
        int day_no = date_calendar.get(Calendar.DAY_OF_MONTH);
        int display_month = date_calendar.get(Calendar.MONTH)+1;
        int display_year = date_calendar.get(Calendar.YEAR);
        int current_month = currentDate.get(Calendar.MONTH)+1;
        int current_year = currentDate.get(Calendar.YEAR);

        View view = convertView;
        if(view ==null){
           view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        if(display_month == current_month && display_year == current_year){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView day_number = view.findViewById(R.id.calendar_day);
        day_number.setText(String.valueOf(day_no));
        
        return view;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
