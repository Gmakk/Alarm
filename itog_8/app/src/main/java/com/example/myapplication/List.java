package com.example.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class List extends ListActivity implements AdapterView.OnItemLongClickListener {

    final String[] catNamesArray = new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> catNamesList = new ArrayList<>(Arrays.asList(catNamesArray));
    private final static String FILE_NAME = "test4.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, catNamesList);
        setListAdapter(mAdapter);
        getListView().setOnItemLongClickListener(this);

        Bundle extra1 = getIntent().getExtras();
        String hour = extra1.getString("KEY1");
        Bundle extra2 = getIntent().getExtras();
        String minute = extra2.getString("KEY2");


        //ЗАПИСЬ В ФАЙЛ


        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILE_NAME, MODE_APPEND)));
            // пишем данные
            bw.write(hour+":"+minute + "\n");
            // закрываем поток
            bw.close();
            //Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //СЧИТЫВАНИЕ ФАЙЛА
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILE_NAME)));
            String str = "";
            int i=0;
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                catNamesList.add(i,str);
                i++;
                //Log.d(LOG_TAG, str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            File file = new File("test.txt");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            catNamesList.add(0,line);
            int i=1;
            while (line != null) {
                System.out.println(line);
                // считываем остальные строки в цикле
                line = reader.readLine();
                catNamesList.add(i,line);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //System.out.println(hour);
        //System.out.println(minute);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//        Toast.makeText(getApplicationContext(),
//                "Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(), "Вы выбрали " + l.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();

        mAdapter.remove(selectedItem);
        mAdapter.notifyDataSetChanged();

        Toast.makeText(getApplicationContext(),
                selectedItem + " удалён.",
                Toast.LENGTH_SHORT).show();
        return true;


        /*public static void deleteServer(int index) {
            StringBuilder builder = new StringBuilder();

            try
            {
                File file = new File(FILE_NAME);
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line;

                while ((line = reader.readLine()) != null) {
                    if (line != "")
                        builder.append(line+"\n");;
                }

                FileWriter writer = new FileWriter(file);
                writer.write(builder.toString());
                writer.close();
                reader.close();
            }
            catch (IOException e)
            {

            }*/
    }

}





