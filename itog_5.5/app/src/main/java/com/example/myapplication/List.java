package com.example.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class List extends ListActivity implements AdapterView.OnItemLongClickListener {

    final String[] catNamesArray = new String[]{"7:00", "8:00", "8:30",
            "9:00", "9:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30"};

    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> catNamesList = new ArrayList<>(Arrays.asList(catNamesArray));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, catNamesList);
        setListAdapter(mAdapter);
        getListView().setOnItemLongClickListener(this);

        /*Bundle arguments1 = getIntent().getExtras();
        String hour = arguments1.get("hour_1").toString();
        Bundle arguments2 = getIntent().getExtras();
        String minute = arguments2.get("minute_1").toString();
        System.out.println(hour);
        System.out.println(minute);*/
        String hour;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                hour= null;
            } else {
                hour= extras.getString("hour_1");
            }
        } else {
            hour= (String) savedInstanceState.getSerializable("hour_1");
        }
        String minute;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                minute= null;
            } else {
                minute= extras.getString("minute_1");
            }
        } else {
            minute= (String) savedInstanceState.getSerializable("minute_1");
        }

        System.out.println(hour);
        System.out.println(minute);
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
    }
}





