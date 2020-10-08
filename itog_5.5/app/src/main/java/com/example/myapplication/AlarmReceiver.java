package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmReceiver extends BroadcastReceiver  {
    /*Context mContext;
    public  AlarmReceiver(Context mContext){
        this.mContext = mContext;
    }*/
    @Override
    public void onReceive(Context context, Intent intent) {

        /*long[] pattern = {0, 400, 200, 400};
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(pattern, 2);
        }*/

        /*Bundle arguments1 = getIntent().getExtras();
        String hour = arguments1.get("hour_1").toString();
        Bundle arguments2 = getIntent().getExtras();
        String minute = arguments2.get("minute_1").toString();
        System.out.println(hour);
        System.out.println(minute);*/


        Intent i = new Intent();
        i.setClassName("com.example.myapplication", "com.example.myapplication.Alarm");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
}
}

