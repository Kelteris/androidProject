package com.cs246.rmgroup.rmplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        list.add("Pray");
        list.add("Repent");
        list.add("Give Stuff");

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_selectable_list_item,
                list);

        listView.setAdapter(adapter);


    }


}
