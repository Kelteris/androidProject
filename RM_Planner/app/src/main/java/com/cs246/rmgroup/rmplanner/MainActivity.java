package com.cs246.rmgroup.rmplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    boolean isMainActivity = true;
    int[] hours = {7, 8, 9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    String[] strHours = {"7:00", "8:00", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
            "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00",
            "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00"};
    static TextView dateView;
    ArrayAdapter<String> adapter;
    FlyOutContainer root;
    ListView listView;
    GridLayout gLayout = null;
    LinearLayout leftLayout;
    LinearLayout mainLayout;
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
        dPicker.init(thisDay.get(Calendar.YEAR),
                    thisDay.get(Calendar.MONTH),
                    thisDay.get(Calendar.DAY_OF_MONTH),
                    new MyOnDateChangeListener());
        Log.d("DATE", Integer.toString(thisDay.get(Calendar.YEAR)) +
        ", " + Integer.toString(thisDay.get(Calendar.MONTH)) +
        ", " + Integer.toString(thisDay.get(Calendar.DAY_OF_MONTH)));
        dateView.setText(getString(R.string.date_format,
                new DateFormatSymbols().getMonths()[thisDay.get(Calendar.MONTH) -1],
                thisDay.get(Calendar.DAY_OF_MONTH),
                thisDay.get(Calendar.YEAR)));
        //dateView.setText("Date: %s/%d/%d", Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        //"Date: " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year

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

    public void toggleMenu(View v) {
        this.root.toggleMenu();
        isMainActivity ^= true;

        ImageButton ib = (ImageButton) findViewById(R.id.menuButton);
        if (isMainActivity) {
            ib.setBackgroundResource(R.drawable.arrow_right);
        } else {
            ib.setBackgroundResource(R.drawable.arrow_left);
        }
    }

    void buildPlannerView() {
        //Setup gridLayout
        gLayout.setColumnCount(2);
        gLayout.setRowCount(strHours.length);

        /************************************
         * Bring in all hours in left column
         ***********************************/
        for (int i = 0; i < strHours.length; i++) {
            GridLayout.LayoutParams params = new
                    GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(0, GridLayout.CENTER));
            params.setMargins(5, 0, 0, 0);
            params.setGravity(Gravity.FILL);
            TextView tv = new TextView(this);
            //tv.setText(Integer.toString(hours[i]) + ((i < 5) ? "AM" : "PM"));
            tv.setText(strHours[i]);
            tv.setTextSize(pixelsToDp(35, this));
            tv.setPadding(10,10,10,10);
            gLayout.addView(tv, params);
        }

        /*******************************
         * Bring in all editTexts
         ******************************/
        for (int i = 0; i < strHours.length; i++) {
            GridLayout.LayoutParams params = new
                    GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(1, GridLayout.LEFT));
            params.setGravity(Gravity.FILL_HORIZONTAL);
            EditText et = new EditText(this);
            et.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            et.setTextSize(pixelsToDp(55, this));
            et.setText(" ");
            et.setPadding(1, 3, 3, 5);
            gLayout.addView(et, params);
        }

        /******************************************************/
        //Apply drawable to all
        int count = 0;
        count = gLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = gLayout.getChildAt(i);
            v.setBackgroundResource(R.drawable.draw_back_left);
            //v.setBackgroundColor(Integer.parseInt("ACACAC", 16) + (50 * i));
            if (gLayout.getChildAt(i) instanceof EditText) {
                EditText e = (EditText) gLayout.getChildAt(i);
                e.setBackgroundResource(R.drawable.draw_back);
            }
        }
        /******************************************************/
    }

    public static float pixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
