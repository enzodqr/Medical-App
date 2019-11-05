package com.app.medical.Alertas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.medical.R;

import java.util.Calendar;

public class NuevaAlarma extends AppCompatActivity implements View.OnClickListener {

    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_alarma);

        //set OnClick Listener
        findViewById(R.id.btn_set_alarm).setOnClickListener(this);
        findViewById(R.id.btn_cancel_alarm).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        EditText nombreAlarma = findViewById(R.id.nombre_alarma);
        TimePicker timePicker = findViewById(R.id.alarm_time_picker);

        /*set notification and text*/
        Intent intent = new Intent(NuevaAlarma.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("todo", nombreAlarma.getText().toString());

        //getBroadcast(context, requestCode, intent, flags)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(NuevaAlarma.this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        switch(view.getId()){
            case R.id.btn_set_alarm:
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                //create time
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                //setAlarm: set(type, milliseconds, intent)
                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                Toast.makeText(this, "Alarma Guardada", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_cancel_alarm:
                alarm.cancel(alarmIntent);
                Toast.makeText(this, "Alarma Cancelada", Toast.LENGTH_SHORT).show();
                break;

        } //cierre del switch

    }
}
