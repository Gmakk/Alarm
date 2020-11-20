package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    TextView tv_Steps;
    boolean isRunning = false;
    String steps="0";


    Button alarm_on, alarm_off, list;
    TextView updateText;
    EditText hour1;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private final static String FILE_NAME = "count6.txt";
    private final static String FILE_XLSX = "filetoshare4.xlsx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tv_Steps = (TextView) findViewById(R.id.textView_Steps);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
            }
        }

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

                if(Integer.parseInt(hour1.getText().toString())>24 || Integer.parseInt(hour1.getText().toString())<0||Integer.parseInt(minute1.getText().toString())>60 || Integer.parseInt(minute1.getText().toString())<0)
                    Toast.makeText(getApplicationContext(),"Введите корректное время", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(MainActivity.this, List.class);
                    Bundle d = new Bundle();
                    d.putString("KEY1", hour_string.toString());
                    intent.putExtras(d);

                    Bundle t = new Bundle();
                    t.putString("KEY2", minute_string.toString());
                    intent.putExtras(t);

                    startActivity(intent);

                    System.out.println(hour_string);
                    System.out.println(minute_string);

                    if (Integer.parseInt(minute1.getText().toString()) < 10)
                        minute_string = "0" + String.valueOf(Integer.parseInt(minute1.getText().toString()));
                    updateText.setText("Будильник поставлен на " + hour_string + ":" + minute_string);

                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                /*Intent intent = new Intent(MainActivity.this , List.class);
                Bundle d=new Bundle();
                d.putString("KEY1",file.toString());
                intent.putExtras(d);
                startActivity(intent);*/
                }
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
                //System.out.println(currentDate);
                EditText hour2 = (EditText) findViewById(R.id.hour);
                EditText minute2 = (EditText) findViewById(R.id.minute);
                DateFormat dateFormat1 = DateFormat.getDateInstance(DateFormat.MEDIUM);
                DateFormat dateFormat2 = DateFormat.getDateInstance(DateFormat.SHORT);

                String date = dateFormat1.format(currentDate);
                String time = dateFormat2.format(currentDate);

                DateFormat df = new SimpleDateFormat("HH:mm");
                String date3 = df.format(Calendar.getInstance().getTime());


                try {
                    File file = new File("/data/data/com.example.myapplication/cache/"+FILE_XLSX);
                    if(!(file.exists())) {
                        XSSFWorkbook workbook = new XSSFWorkbook();
                        XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"));
                        Row row = sheet.createRow(0);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(date);
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(time);
                        Cell cell2 = row.createCell(2);
                        cell2.setCellValue(String.valueOf(hour2.getText())); //hour2.getText()
                        Cell cell3 = row.createCell(3);
                        cell3.setCellValue(String.valueOf(minute2.getText()));//minute2.getText()
                        Cell cell4 = row.createCell(4);
                        cell4.setCellValue(date3);
                        Cell cell5 = row.createCell(5);
                        cell5.setCellValue(steps);
                        File cacheDir = getCacheDir();
                        File outFile = new File(cacheDir, FILE_XLSX);
                        OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
                        workbook.write(outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                    else
                    {
                        File myFile_ED = new File("/data/data/com.example.myapplication/cache/" + FILE_XLSX);
                        FileInputStream inputStream_ED = new FileInputStream(myFile_ED);

                        XSSFWorkbook workbook_ED = new XSSFWorkbook(inputStream_ED);
                        XSSFSheet sheet_ED = workbook_ED.getSheetAt(0);
                        Iterator<Row> riterator_ED = sheet_ED.iterator();
                        Row row_ED = sheet_ED.createRow(sheet_ED.getLastRowNum() + 1);
                        if (sheet_ED.getLastRowNum() == 0) {

                        }

                        Cell cell = row_ED.createCell(0);
                        cell.setCellValue(date);
                        Cell cell1 = row_ED.createCell(1);
                        cell1.setCellValue(time);
                        Cell cell2 = row_ED.createCell(2);
                        cell2.setCellValue(String.valueOf(hour2.getText())); //hour2.getText()
                        Cell cell3 = row_ED.createCell(3);
                        cell3.setCellValue(String.valueOf(minute2.getText()));//minute2.getText()
                        Cell cell4 = row_ED.createCell(4);
                        cell4.setCellValue(date3);
                        Cell cell5 = row_ED.createCell(5);
                        cell5.setCellValue(steps);


                        FileOutputStream os_ED = new FileOutputStream(myFile_ED);
                        workbook_ED.write(os_ED);

                        os_ED.close();
                        workbook_ED.close();
                        inputStream_ED.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null)
        {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            // Toast.makeText(this, "STEP_COUNTER sensor not found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        isRunning = false;
//    sensorManager.unregisterListener(this); // this will stop detecting steps!
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isRunning)
        {
            steps=String.valueOf(sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}