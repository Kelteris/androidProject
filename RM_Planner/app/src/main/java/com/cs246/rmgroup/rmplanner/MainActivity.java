package com.cs246.rmgroup.rmplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                new DateFormatSymbols().getMonths()[thisDay.get(Calendar.MONTH)],
                thisDay.get(Calendar.DAY_OF_MONTH),
                thisDay.get(Calendar.YEAR)));

        buildPlannerView();
    }

    //How we add to the "to-do" list
    protected void addToList(View v) {
        final EditText taskEditText = new EditText(this);
        Log.i("created a diaolg", "creating box");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        if (task.length() > 0) {
                            list.add(task);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "NO CONTENT", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        Log.i("created a dialog", "dialog showing");
    }

    //Fancy version that we'll use
    private class MyOnDateChangeListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
            dateView.setText(getString(R.string.date_format,
                    new DateFormatSymbols().getMonths()[monthOfYear],
                    dayOfMonth,
                    year));
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
            //String displayText = ("Selected Date: " + (month + 1) + "/" + day + "/" + year);
            //dateView.setText(displayText);
            dateView.setText(getString(R.string.date_format,
                    new DateFormatSymbols().getMonths()[month],
                    day,
                    year));
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
    }

    void buildPlannerView() {
        //Setup gridLayout
        Log.d("Left Column hour view", "Started creating the hour view");
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
            tv.setPadding(10, 10, 10, 10);
            tv.setId(i + 100);
            gLayout.addView(tv, params);
            Log.d("Left column hour view", "Created the hours of the day");
        }

        /*******************************
         * Bring in all editTexts
         ******************************/
        Log.e("Hours being created", "Right now");
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
            et.setText(null);
            et.setPadding(10, 3, 3, 5);
            //et.setPaddingRelative(10, 3, 3, 10);
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

        listView = (ListView) findViewById(R.id.list_todo);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_selectable_list_item,
                list);

        listView.setAdapter(adapter);
    }

    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
