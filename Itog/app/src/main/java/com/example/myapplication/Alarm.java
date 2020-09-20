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
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class Alarm  extends AppCompatActivity implements LocationListener {

    private TextView temp1;
    private TextView wind1;
    private TextView rain1;
    private TextView weather1;

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
        setContentView(R.layout.activity_alarm);

        TextView sum = findViewById(R.id.sum);
        TextView sum2 = findViewById(R.id.sum2);


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        sum.setText("Double.toString(latitude)");
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude=loc.getLatitude();
        double longitude=loc.getLongitude();



        Intent intent = getIntent();


        //TextView sum = findViewById(R.id.sum);
        if(sum == null)
            Log.d("","Поле sum не найдено");
        else
            sum.setText(Double.toString(latitude));
        //TextView sum2 = findViewById(R.id.sum2);
        if(sum2 == null)
            Log.d("","Поле sum2 не найдено");
        else
            sum2.setText(Double.toString(longitude));

        temp1 = findViewById(R.id.temp1);
        wind1 = findViewById(R.id.wind1);
        rain1 = findViewById(R.id.rain1);
        weather1 = findViewById(R.id.weather);

        double lat = latitude;
        double lon = longitude;
        new WeatherTask(lat, lon).execute();
    }

    private class WeatherTask extends AsyncTask<Void, Void, String> {

        private  double lat;
        private  double lon;
        private String key = "650cf34e7d98d008f477f7ff553402c1";

        public WeatherTask(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String content = getConternt("https://api.openweathermap.org/data/2.5/onecall?lat="+ lat +"&lon="+ lon +"&exclude=hourly&exclude=minutely&exclude=daily&appid=" + key);
            return content;
        }

        protected void onPostExecute(String content){
            try{

                Log.d("", "Широта=" + lat);
                Log.d("", "Долгота=" + lon);

                rain1.setText("Осадков нет");

                JSONObject json = new JSONObject(content);
                double temp = json.getJSONObject("current").getDouble("temp")-273;
                temp1.setText("Температура "  + temp + "\u2103");
                System.out.println("Температура "  + temp + "\u2103");

                double wind = json.getJSONObject("current").getDouble("wind_speed");
                wind1.setText("Скорость ветра " + wind + "м/c");


                if(temp<25 && temp>20 && wind<=7.9)
                    weather1.setText("Сейчас хорошая погода");
                else
                    weather1.setText("Сейчас плохая погода");

                double rain = json.getJSONObject("current").getJSONObject("rain").getDouble("1h");
                rain1.setText("Осадков нет");
                //if(rain != 0)
                    //rain1.setText("Количество осадков " + rain + "мм");
               // else
                   // rain1.setText("Осадков нет");


            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String getConternt (String path){
            try{
                URL url=new URL(path);
                HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(20000);
                c.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String content = "";
                String line = "";
                while ((line = reader.readLine()) != null){
                    content += line + "\n";
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public void clickButton(View view) {
        /*Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }*/
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}