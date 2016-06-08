package com.cs246.rmgroup.rmplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> adapter;
    FlyOutContainer root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /******************************************************/
        //Apply drawable to all
        //GridLayout layout = (GridLayout) findViewById(R.id.GridLayout);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearMain);
        int count = 0;
        try {
            count = layout.getChildCount();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        System.out.print("WHY WON'T YOU RUN?? " + count);
        for (int i = 0; i < count; i++) {
            System.out.print("We've just iterated " + i);
            View v = layout.getChildAt(i);
            v.setBackgroundResource(R.drawable.draw_back);
        }
        /******************************************************/

        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_main, null);

        this.setContentView(root);

        listView = (ListView) findViewById(R.id.listView);

        list.add("Pray");
        list.add("Repent");
        list.add("Give Stuff");
        list.add("Be Happy");

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_selectable_list_item,
                list);

        listView.setAdapter(adapter);

    }

    public void toggleMenu(View v) {
        this.root.toggleMenu();
    }
}
