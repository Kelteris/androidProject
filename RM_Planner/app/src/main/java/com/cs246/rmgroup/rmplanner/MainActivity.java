package com.cs246.rmgroup.rmplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    int[] hours = {7, 8, 9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    ListView listView;
    ArrayAdapter<String> adapter;
    FlyOutContainer root;
    GridLayout gLayout = null;
    LinearLayout leftLayout;
    LinearLayout mainLayout;
    static TextView dateView;
    DatePicker dPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_main, null);
        this.setContentView(root);
        leftLayout = (LinearLayout) findViewById(R.id.sideLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        gLayout = (GridLayout) findViewById(R.id.gridLayout);
        dateView = (TextView) findViewById(R.id.dateView);
        dPicker = (DatePicker) findViewById(R.id.datePicker);

        Calendar thisDay = Calendar.getInstance();
        /*dPicker.init(thisDay.get(Calendar.YEAR),
                    thisDay.get(Calendar.MONTH),
                    thisDay.get(Calendar.DAY_OF_MONTH),
                    new MyOnDateChangeListener());*/
        dPicker.init(1993, 0, 21, new MyOnDateChangeListener());

        buildPlannerView();

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

    //Fancy version that we'll use
    private class MyOnDateChangeListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
            String displayText = ("Date: "
                    + (monthOfYear + 1) +
                    "/" + dayOfMonth + "/" + year);
            dateView.setText(displayText);
            toggleMenu(null);
        }
    }

    //Lame version
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String displayText = ("Selected Date: " + (month + 1) + "/" + day + "/" + year);
            dateView.setText(displayText);
        }
    }
    //Part of lame version
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /*    //**
     *
     * @param datePicker
     * @return a java.util.Date
     *//*
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }*/

    public void toggleMenu(View v) {
        this.root.toggleMenu();
    }

    void buildPlannerView() {
        //Setup gridLayout
        gLayout.setColumnCount(2);
        gLayout.setRowCount(hours.length);

        /************************************
         * Bring in all hours in left column
         ***********************************/
        for (int i = 0; i < hours.length; i++) {
            GridLayout.LayoutParams params = new
                    GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.LEFT),
                    GridLayout.spec(0, GridLayout.CENTER));
            params.setMargins(10, 10, 10, 10);
            params.setGravity(Gravity.FILL);
            TextView tv = new TextView(this);
            tv.setText(Integer.toString(hours[i]) + ((i < 5) ? "AM" : "PM"));
            tv.setPadding(20, 20, 20, 20);
            gLayout.addView(tv, params);
        }

        /*******************************
         * Bring in all editTexts
         ******************************/
        for (int i = 0; i < hours.length; i++) {
            GridLayout.LayoutParams params = new
                    GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(1, GridLayout.LEFT));
            params.setGravity(Gravity.FILL_HORIZONTAL);
            EditText et = new EditText(this);
            et.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            et.setText(" ");
            gLayout.addView(et, params);
        }

        /******************************************************/
        //Apply drawable to all
        int count = 0;
        count = gLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = gLayout.getChildAt(i);
            v.setBackgroundResource(R.drawable.draw_back_left);
            if (gLayout.getChildAt(i) instanceof EditText) {
                EditText e = (EditText) gLayout.getChildAt(i);
                e.setBackgroundResource(R.drawable.draw_back);
            }
        }
        /******************************************************/
    }
}
