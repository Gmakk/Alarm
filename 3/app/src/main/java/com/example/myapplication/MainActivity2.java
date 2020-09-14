package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;


public class MainActivity2 extends AppCompatActivity implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        LocationManager lm =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude=loc.getLatitude();
        double longitude=loc.getLongitude();
        Log.d("", "Широта=" + latitude);
        Log.d("", "Долгота=" + longitude);


         Intent intent = getIntent();


         TextView sum = findViewById(R.id.sum);
         if(sum == null)
             Log.d("","Поле sum не найдено");
         else
             sum.setText(Double.toString(latitude));
         TextView sum2 = findViewById(R.id.sum2);
         if(sum2 == null)
            Log.d("","Поле sum2 не найдено");
         else
            sum2.setText(Double.toString(longitude));

    }


    public void clickButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}