package com.cs246.rmgroup.rmplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.RelativeLayout;
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
    String[] strHours = {"7:00", "8:00", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
            "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00",
            "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00"};
    static TextView dateView;
    static Context baseContext;
    static EditText notes;
    static Calendar currentDay;
    ArrayAdapter<String> adapter;
    FlyOutContainer root;
    ListView listView;
    GridLayout gLayout = null;
    LinearLayout leftLayout;
    LinearLayout mainLayout;
    DatePicker dPicker;
    Logging log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_main, null);
        this.setContentView(root);
        baseContext = this.getApplicationContext();
        leftLayout = (LinearLayout) findViewById(R.id.sideLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        gLayout = (GridLayout) findViewById(R.id.gridLayout);
        dateView = (TextView) findViewById(R.id.dateView);
        dPicker = (DatePicker) findViewById(R.id.datePicker);
        notes = (EditText) findViewById(R.id.notes);
        currentDay = Calendar.getInstance();
        dPicker.init(currentDay.get(Calendar.YEAR),
                currentDay.get(Calendar.MONTH),
                currentDay.get(Calendar.DAY_OF_MONTH),
                new MyOnDateChangeListener());
        Log.d("DATE", Integer.toString(currentDay.get(Calendar.YEAR)) +
                ", " + Integer.toString(currentDay.get(Calendar.MONTH)) +
                ", " + Integer.toString(currentDay.get(Calendar.DAY_OF_MONTH)));
        dateView.setText(getString(R.string.date_format,
                new DateFormatSymbols().getMonths()[currentDay.get(Calendar.MONTH)],
                currentDay.get(Calendar.DAY_OF_MONTH),
                currentDay.get(Calendar.YEAR)));

        log = Logging.getInstance();

        buildPlannerView();
        setUpListeners();
        lookupNote(null);
    }

    //How we add to the "to-do" list

    /**
     * Adds goals to the "to-do" list
     *
     * @param v
     * @author Travis Confer
     */
    public void addToList(View v) {
        final EditText taskEditText = new EditText(this);
        Log.i("created a dialog", "creating box");
        String string = "created a dialog" + "creating box";
        log.insertLog(string);
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

    //Swipe Gestures Listener Setup
    private void setUpListeners() {
        mainLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeRight() {
                if (isMainActivity) {
                    Log.d("SWIPE", "Swiping right!");
                    toggleMenu(null);
                }
            }

            @Override
            public void onSwipeLeft() {
                if (!isMainActivity) {
                    Log.d("SWIPE", "Swiping left!");
                    toggleMenu(null);
                }
            }
        });

        leftLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeLeft() {
                if (!isMainActivity) {
                    Log.d("SWIPE", "Swiping left!");
                    toggleMenu(null);
                }
            }
        });

        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {   }

            @Override
            public void afterTextChanged(Editable s) {
                saveNote(null);
            }
        });

        for (int i = 0; i < strHours.length; i++){
            final EditText et = (EditText) findViewById(300 + i);

            if (et != null) {
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {   }

                    @Override
                    public void afterTextChanged(Editable s) {
                        saveEvent(et);
                    }
                });
            }
        }
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
            lookupNote(null);
        }
    }

    //Lame Version for Dialog
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year = currentDay.get(Calendar.YEAR);
            int month = currentDay.get(Calendar.MONTH);
            int day = currentDay.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateView.setText(getString(R.string.date_format,
                    new DateFormatSymbols().getMonths()[month],
                    day,
                    year));
            currentDay.set(year, month, day);
            lookupNote(null);
        }
    }

    //Part of lame version
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Switches between FlyOutContainer and MainActivity
     *
     * @param v
     * @author Chris Simmons
     */
    public void toggleMenu(View v) {
        this.root.toggleMenu();
        isMainActivity ^= true;
    }

    /**
     * Builds the hourly view, and the reminder option for added benefit
     *
     * @author Travis Confer
     * @author Asa Skousen
     */
    void buildPlannerView() {
        //Setup gridLayout
        Log.d("Left Column hour view", "Started creating the hour view");
        gLayout.setColumnCount(3);
        gLayout.setRowCount(strHours.length);

        /************************************
         * Bring in all hours in left column
         ***********************************/
        Log.d("BUILD", "Creating left column hours");
        int color;
        float ratio = 0;
        for (int i = 0; i < strHours.length; i++) {
            GridLayout.LayoutParams params = new
                    GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(0, GridLayout.CENTER));
            params.setMargins(5, 0, 0, 0);
            params.setGravity(Gravity.FILL);
            TextView tv = new TextView(this);
            tv.setText(strHours[i]);
            tv.setTextSize(pixelsToDp(35, this));
            tv.setTextColor(Color.BLACK);
            tv.setPadding(10, 15, 5, 10);
            tv.setId(i + 100);
            //Set Drawable
            Drawable background = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.draw_back_left, null);

            if (i < (strHours.length / 2)) {
                ratio = (i / (float) (strHours.length / 2));
                color = mixTwoColors(
                        ContextCompat.getColor(tv.getContext(), R.color.colorNoon),
                        ContextCompat.getColor(tv.getContext(), R.color.colorMorning),
                        ratio);
            } else {
                ratio = ((i - (strHours.length / 2)) / (float) (strHours.length / 2));
                color = mixTwoColors(
                        ContextCompat.getColor(tv.getContext(), R.color.colorNight),
                        ContextCompat.getColor(tv.getContext(), R.color.colorNoon),
                        ratio);
            }
            //Blend background
            background.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            tv.setBackground(background);
            gLayout.addView(tv, params);
        }

        /*******************************
         * Bring in all editTexts
         ******************************/
        Log.d("BUILD", "Entering EditTexts");
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
            et.setId(i + 300);
            et.setEms(8);
            // prevents the editText from pushing the button out of view, but doesn't do anything else
            et.setMaxWidth(10);
            //Set Drawable
            et.setBackgroundResource(R.drawable.draw_back);
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    ImageButton tmpBtn = (ImageButton) findViewById(v.getId() - 100);
                    // only checks if the line is full on focusChange, not as soon as anything is typed in.
                    if (tmpBtn != null && hasFocus) {
                        tmpBtn.setVisibility(View.VISIBLE);
                        Log.d("COG", "Detected a focus change");
                    } else if (tmpBtn != null) {
                        tmpBtn.setVisibility(View.GONE);
                        Log.d("COG", "Lost focus");
                    } else {
                        // do nothing, the object is null
                    }
                }
            });
            gLayout.addView(et, params);
        }

        /*******************************************
         * Creates the option menu button
         ******************************************/
        for (int i = 0; i < strHours.length; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(1, GridLayout.CENTER));
            params.setMargins(0, 0, 0, 0);
            params.setGravity(Gravity.FILL_VERTICAL);
            ImageButton btn = new ImageButton(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setPadding(0, 0, 0, 0);
            btn.setId(i + 200);
            btn.setBackgroundColor(Color.TRANSPARENT);

            //btn.setBackgroundResource(R.drawable.gear_option);
            btn.setImageResource(R.drawable.gear_option);
            gLayout.addView(btn, params);
            btn.setVisibility(View.GONE);

        }

        /******************************************************/

        listView = (ListView) findViewById(R.id.list_todo);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_selectable_list_item,
                list);
        listView.setAdapter(adapter);
    }


    public static int mixTwoColors(int color1, int color2, float amount) {
        //Bitshift values for each hexColor
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL = 16;
        final byte GREEN_CHANNEL = 8;
        final byte BLUE_CHANNEL = 0;

        final float inverseAmount = 1.0f - amount;
        int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
        int b = ((int) (((float) (color1 & 0xff) * amount) +
                ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
    }

    /**
     * converts from pixels to Dp.
     *
     * @param px
     * @param context
     * @return
     */
    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public void saveNote(View view) {
        removeNote(null);
        newNote(null);
    }

    public void saveEvent(View view) {
        EditText et = (EditText)view;
        Log.d("EVENT", "Saving ID of: " + Integer.toString(view.getId()));

    }

    public static void lookupNote(View view) {
        MyDBHandler dbHandler = new MyDBHandler(baseContext, null, null, 1);

        Note note = dbHandler.findNote(dateView.getText().toString());
        if (note != null) {
            notes.setText(String.valueOf(note.get_description()));
        } else {
            notes.setText(null);
        }
    }

    public void newNote(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Note note = new Note(dateView.getText().toString(), notes.getText().toString());
        dbHandler.addNote(note);
    }

    public void removeNote(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null,
                null, 1);
        dbHandler.deleteNote(dateView.getText().toString());
    }

    public void lookupEvent(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        Event event = dbHandler.findEvent(dateView.getText().toString());
        /*if (event != null) {
            notes.setText(String.valueOf(event.get_description()));
        } else {
            notes.setText(null);
        }*/
    }

    public void newEvent(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        //Event event = new Event(dateView.getText().toString(), notes.getText().toString(),
        //        index of hour array/* this will be the hour for the event*/);
        //dbHandler.addNote(event);
    }

    public void removeEvent(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null,
                null, 1);
        dbHandler.deleteNote(dateView.getText().toString()/*, index of hour array this will be the hour for the event*/);
    }



    //notes listener thing TextWatcher
    /*notes.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

            String currentDate = Integer.toString(thisDay.get(Calendar.YEAR)) +
                    "-" + Integer.toString(thisDay.get(Calendar.MONTH)) +
                    "-" + Integer.toString(thisDay.get(Calendar.DAY_OF_MONTH));

            Note note = new Note(productBox.getText().toString(), quantity/* descirption  and data*///);

            /*dbHandler.addProduct(product);
            productBox.setText("");
            quantityBox.setText("");
        }
    });*/

    // Something needs to happen here
    //gLayout.setOnFocusChangeListener(R.getViewById().onFocusChangeListener l);
}
