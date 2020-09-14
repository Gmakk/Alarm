package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    private TextView temp1;
    private TextView wind1;
    private TextView snow1;
    private TextView rain1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp1 = findViewById(R.id.temp1);
        wind1 = findViewById(R.id.wind1);
        rain1 = findViewById(R.id.rain1);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat = Double.parseDouble(((EditText) findViewById(R.id.lat)).getText().toString());
                double lon = Double.parseDouble(((EditText) findViewById(R.id.lon)).getText().toString());
                new WeatherTask(lat, lon).execute();
            }
        });
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

                rain1.setText("Осадков нет");

                JSONObject json = new JSONObject(content);
                double temp = json.getJSONObject("current").getDouble("temp")-273;
                temp1.setText("Температура "  + temp + "\u2103");

                double wind = json.getJSONObject("current").getDouble("wind_speed");
                wind1.setText("Скорость ветра " + wind + "м/c");

                double rain = json.getJSONObject("current").getJSONObject("rain").getDouble("1h");
                rain1.setText("Количество осадков " + rain + "мм");

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
}