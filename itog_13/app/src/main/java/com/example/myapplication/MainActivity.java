package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlarmManager;
import android.app.DialogFragment;
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

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import static android.app.ProgressDialog.show;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    DialogFragment dig;

    SensorManager sensorManager;
    TextView tv_Steps;
    boolean isRunning = false;
    String steps="0";


    Button alarm_on, alarm_off, list;
    TextView updateText;
    EditText hour1;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private final static String FILE_XLSX = "filetoshare2.xlsx";

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


                }
            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager.cancel(pendingIntent);
                updateText.setText("Будильник выключен");

                dig = new Dialog();
                dig.show(getFragmentManager(), "dig");
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
                String date = dateFormat1.format(currentDate);
                DateFormat df1 = new SimpleDateFormat("HH:mm");
                String date3 = df1.format(Calendar.getInstance().getTime());
                DateFormat df2 = new SimpleDateFormat("EEEE");
                String time = df2.format(Calendar.getInstance().getTime());
                String time_up=hour2.getText()+":"+minute2.getText();


                try {
                    File file = new File("/data/data/com.example.myapplication/cache/"+FILE_XLSX);
                    if(!(file.exists())) {
                        XSSFWorkbook workbook = new XSSFWorkbook();
                        XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"));
                        Row row = sheet.createRow(0);
                        Cell cell = row.createCell(0);
                        cell.setCellValue("DATE");
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue("DAY");
                        Cell cell2 = row.createCell(2);
                        cell2.setCellValue("STEPS");
                        Cell cell3 = row.createCell(3);
                        cell3.setCellValue("TIME_DOWN");
                        Cell cell4 = row.createCell(4);
                        cell4.setCellValue("TIME_UP");
                        File cacheDir = getCacheDir();
                        File outFile = new File(cacheDir, FILE_XLSX);
                        OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
                        workbook.write(outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
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
                        cell2.setCellValue(steps);
                        Cell cell3 = row_ED.createCell(3);
                        cell3.setCellValue(date3);
                        Cell cell4 = row_ED.createCell(4);
                        cell4.setCellValue(time_up);


                        FileOutputStream os_ED = new FileOutputStream(myFile_ED);
                        workbook_ED.write(os_ED);

                        os_ED.close();
                        workbook_ED.close();
                        inputStream_ED.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }

        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {         //   КОД НА ПИТОНЕ

                if (! Python.isStarted()) {
                    Python.start(new AndroidPlatform(MainActivity.this));
                }
                Python py = Python.getInstance();
                PyObject mod = py.getModule("temp");

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

    //------------------------------------------------------------------------------
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/
    /*
     *
     *
     */
    /*private final int SCRIPT_EXEC_PY = 40001;
    private final String extPlgPlusName = "org.qpython.qpy";
    public static boolean checkAppInstalledByName(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);

            Log.d("QPYMAIN",  "checkAppInstalledByName:"+packageName+" found");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("QPYMAIN",  "checkAppInstalledByName:"+packageName+" not found");

            return false;
        }
    }

    public void onQPyExec(View v) {

        if (checkAppInstalledByName(getApplicationContext(), extPlgPlusName)) {
            Toast.makeText(this, "Sample of calling QPython API", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClassName(extPlgPlusName, "org.qpython.qpylib.MPyApi");
            intent.setAction(extPlgPlusName + ".action.MPyApi");

            Bundle mBundle = new Bundle();
            mBundle.putString("app", "myappid");
            mBundle.putString("act", "onPyApi");
            mBundle.putString("flag", "onQPyExec");            // any String flag you may use in your context
            mBundle.putString("param", "");          // param String param you may use in your context


             // The String Python code, you can put your py file in res or raw or intenet, so that you can get it the same way, which can make it scalable

            EditText codeTxt = (EditText)findViewById(R.id.edit_text);
            String code = codeTxt.getText().toString();
            mBundle.putString("pycode", code);

            intent.putExtras(mBundle);

            startActivityForResult(intent, SCRIPT_EXEC_PY);
        } else {
            Toast.makeText(getApplicationContext(), "Please install QPython first", Toast.LENGTH_LONG).show();

            try {
                Uri uLink = Uri.parse("market://details?id=com.hipipal.qpyplus");
                Intent intent = new Intent( Intent.ACTION_VIEW, uLink );
                startActivity(intent);
            } catch (Exception e) {
                Uri uLink = Uri.parse("http://qpython.com");
                Intent intent = new Intent( Intent.ACTION_VIEW, uLink );
                startActivity(intent);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCRIPT_EXEC_PY) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String flag = bundle.getString("flag"); // flag you set
                String param = bundle.getString("param"); // param you set
                String result = bundle.getString("result"); // Result your Pycode generate
                Toast.makeText(this, "onQPyExec: return (" + result + ")", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "onQPyExec: data is null", Toast.LENGTH_SHORT).show();

            }
        }
    }*/
}