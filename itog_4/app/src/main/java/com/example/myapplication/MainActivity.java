package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button alarm_on, alarm_off, list;
    TextView updateText;
    EditText hour1;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarm_on = (Button) findViewById(R.id.alarm_on);
        alarm_off = (Button) findViewById(R.id.alarm_off);
        list = (Button) findViewById(R.id.list);
        Button sleep1 = (Button) findViewById(R.id.sleep);
        updateText = (TextView) findViewById(R.id.updateTextTime);
        final AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        final Calendar calendar = Calendar.getInstance();

        final Intent my_intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText hour1 = (EditText) findViewById(R.id.hour);
                EditText minute1 = (EditText) findViewById(R.id.minute);
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour1.getText().toString()));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute1.getText().toString()));

                String hour_string = String.valueOf(Integer.parseInt(hour1.getText().toString()));
                String minute_string = String.valueOf(Integer.parseInt(minute1.getText().toString()));

                Bundle b1 = new Bundle();
                b1.putString("hour_1", hour_string);
                Bundle b2 = new Bundle();
                b2.putString("minute_1", minute_string);
                Intent intent1 = new Intent(MainActivity.this, List.class);
                intent1.putExtras(b1);
                Intent intent2 = new Intent(MainActivity.this, List.class);
                intent2.putExtras(b2);
                System.out.println(hour_string);
                System.out.println(minute_string);

                if(Integer.parseInt(minute1.getText().toString()) < 10)
                    minute_string = "0" + String.valueOf(Integer.parseInt(minute1.getText().toString()));
                updateText.setText("Будильник поставлен на "+hour_string+":"+minute_string);

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);

            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager.cancel(pendingIntent);
                updateText.setText("Будильник выключен");
            }
        });
        sleep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                System.out.println(currentDate);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, List.class);
                startActivity(intent3);
            }
        });
    }

    public void clickButton(View view) {
        Intent intent = new Intent(this, Tracker.class);
        startActivity(intent);
    }
}