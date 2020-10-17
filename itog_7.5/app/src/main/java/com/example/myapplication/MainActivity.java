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

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;





import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
//import org.o7planning.apachepoiexcel.model.Employee;
//import org.o7planning.apachepoiexcel.model.EmployeeDAO;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class MainActivity extends AppCompatActivity {

    Button alarm_on, alarm_off, list;
    TextView updateText;
    EditText hour1;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private static final String FILE_NAME = "MyFirstExcel.xlsx";

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


                Intent intent = new Intent(MainActivity.this , List.class);
                Bundle d=new Bundle();
                d.putString("KEY1",hour_string.toString());
                intent.putExtras(d);

                Bundle t=new Bundle();
                t.putString("KEY2",minute_string.toString());
                intent.putExtras(t);

                startActivity(intent);

                System.out.println(hour_string);
                System.out.println(minute_string);

                if(Integer.parseInt(minute1.getText().toString()) < 10)
                    minute_string = "0" + String.valueOf(Integer.parseInt(minute1.getText().toString()));
                updateText.setText("Будильник поставлен на "+hour_string+":"+minute_string);

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);

                /*Intent intent = new Intent(MainActivity.this , List.class);
                Bundle d=new Bundle();
                d.putString("KEY1",file.toString());
                intent.putExtras(d);
                startActivity(intent);*/
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

                    //printlnToUser("writing xlsx file");
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"));

                    Row row = sheet.createRow(0);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(date);
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(time);
                    Cell cell2 = row.createCell(2);
                    cell2.setCellValue(1); //hour2.getText()
                    Cell cell3 = row.createCell(3);
                    cell3.setCellValue(1);//minute2.getText()
                    Cell cell4 = row.createCell(4);
                    cell4.setCellValue(date3);
                   /* for (int i=0;i<10;i++) {
                        Row row = sheet.createRow(i);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(i);
                    }*/
                    String outFileName = "filetoshare.xlsx";
                    try {
                        //printlnToUser("writing file " + outFileName);
                        File cacheDir = getCacheDir();
                        File outFile = new File(cacheDir, outFileName);
                        OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
                        System.out.println(outFile.getAbsolutePath());
                        workbook.write(outputStream);
                        outputStream.flush();
                        outputStream.close();
                       // printlnToUser("sharing file...");
                        //share(outFileName, getApplicationContext());
                    } catch (Exception e) {
                        /* proper exception handling to be here */
                        //printlnToUser(e.toString());
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
}