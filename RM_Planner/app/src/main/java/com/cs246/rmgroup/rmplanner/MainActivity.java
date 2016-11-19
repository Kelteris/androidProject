package com.cs246.rmgroup.rmplanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaFormat;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> list = new ArrayList<>();
    boolean isMainActivity = true;
    boolean isLoading = false;
    static String[] strHours = {"7:00", "8:00", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30", "1:00", "1:30", "2:00", "2:30",
            "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00",
            "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30",
            "11:00", "11:30", "12:00"};
    static Activity baseActivity;
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
        baseActivity = this;
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

        Set<String> tasksSet = PreferenceManager.getDefaultSharedPreferences(baseContext)
                .getStringSet("tasks_set", new HashSet<String>());

        list = new ArrayList<String>(tasksSet);
        buildPlannerView();
        setUpListeners();
        lookupNote(null);
        lookupEvent();
    }


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
                            saveGoals(list);
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
    protected void setUpListeners() {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                if (list.get(position) != null) {
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                    saveGoals(list);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveNote(null);
            }
        });

        for (int i = 0; i < strHours.length; i++) {
            final EditText et = (EditText) findViewById(300 + i);
            /*Uncomment below to allow the cog to show for options*/
            //final ImageButton btn = (ImageButton) findViewById(200 + i);
            if (et != null) {
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        saveEvent(et);
                    }
                });
            }

            /* Uncomment for button to show and be used
            if (btn != null) {
                final int textLocation = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        showEventOptions(textLocation);
                    }
                });
            }*/
        }
    }

    /**
     * Inserts reminder for event
     *
     * @param textLocation
     * @author Asa Skousen
     */
    public void showEventOptions(final int textLocation) {
        CharSequence options[] = new CharSequence[]{"Set Reminder"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        createReminder(textLocation);
                    default:
                        Log.d("OPTIONS", "Default was run");
                }
            }
        });
        builder.show();
    }

    public void createReminder(int textLocation) {
        EditText editText = (EditText) findViewById(300 + textLocation);
        String string = editText.getText().toString();
        if (string.matches("")) {
            Log.v("Create Reminder", "an attempt to create an empty reminder was made");
        } else {
            string = string + " at " + strHours[textLocation];
            if (textLocation < 8 || textLocation == 33) {
                string = string + " AM";
            } else {
                string = string + " PM";
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.rm_planner_icon)
                            .setContentTitle("RM Planner")
                            .setContentText(string);
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, MainActivity.class);
            mBuilder.setAutoCancel(true);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // 500 is the id, use it if you want to update it
            mNotificationManager.notify(500, mBuilder.build());
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
            lookupNote(null);
            lookupEvent();
            toggleMenu(null);
        }
    }

    //Lame Version for Dialog
    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment
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
            lookupEvent();
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
        gLayout.setColumnCount(2);
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
            //tv.setTextSize(pixelsToDp(35, this));
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(13);
            tv.setTextColor(Color.BLACK);
            tv.setPadding(10, 15, 5, 10);
            tv.setId(i + 100);

            Drawable background;
            //Set Drawable
            if (i != 0 && i != strHours.length - 1) {
                background = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.draw_hours_middle, null);
            } else if (i == (strHours.length - 1)){
                background = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.draw_hours_bottom, null);
            } else {
                background = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.draw_hours_top, null);
            }

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
            et.setTextSize(20);
            et.setText(null);
            et.setPadding(10, 3, 3, 5);
            et.setId(i + 300);
            et.setEms(8); // This just prevents text from going off the screen
            et.setMaxWidth(10);
            et.setFilters( new InputFilter[] {new InputFilter.LengthFilter(50)});

            et.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            et.setSingleLine(false);
            et.setHorizontallyScrolling(false);
            et.setMaxLines(Integer.MAX_VALUE);
            et.setImeOptions(EditorInfo.IME_ACTION_DONE);


            //Set Drawable
            et.setBackgroundResource(R.drawable.draw_back);
            /*et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    ImageButton tmpBtn = (ImageButton) findViewById(v.getId() - 100);
                    if (tmpBtn != null && hasFocus) {
                        tmpBtn.setVisibility(View.VISIBLE);
                        Log.d("COG", "Became the focus");
                    } else if (tmpBtn != null) {
                        tmpBtn.setVisibility(View.GONE);
                        Log.d("COG", "Lost the focus");
                    } else {
                        Log.i("COG", "The object was null");
                        // do nothing, the object is null
                    }
                }
            });*/
            gLayout.addView(et, params);
        }

        /*******************************************
         * Creates the option menu button
         ******************************************/
        for (int i = 0; i < strHours.length; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(i, GridLayout.CENTER),
                    GridLayout.spec(1, GridLayout.CENTER));
            params.setMargins(430, 0, 0, 0);
            params.setGravity(Gravity.FILL_VERTICAL);
            ImageButton btn = new ImageButton(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setPadding(0, 0, 0, 0);
            btn.setId(i + 200);
            btn.setBackgroundColor(Color.TRANSPARENT);
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

    /**
     * Mix two colors to use in porter_duff blender
     *
     * @param color1
     * @param color2
     * @param amount
     * @author Travis Confer
     */
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
     *
     * @author Travis Confer
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

    public void saveGoals(ArrayList<String> goalList) {
        Set<String> tasksSet = new HashSet<String>(goalList);
        PreferenceManager.getDefaultSharedPreferences(baseContext)
                .edit()
                .putStringSet("tasks_set", tasksSet)
                .commit();
    }

    public void saveEvent(View view) {
        if (!isLoading) {
            removeEvent((EditText) view);
            newEvent((EditText) view);
        }
    }

    public static void lookupNote(View view) {
        MyDBHandler dbHandler = new MyDBHandler(baseContext, null, null, 4);

        Note note = dbHandler.findNote(dateView.getText().toString());
        if (note != null) {
            notes.setText(String.valueOf(note.get_description()));
        } else {
            notes.setText(null);
        }
    }

    public void newNote(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 4);
        Note note = new Note(dateView.getText().toString(), notes.getText().toString());
        dbHandler.addNote(note);
    }

    public void removeNote(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null,
                null, 3);
        dbHandler.deleteNote(dateView.getText().toString());
    }

    public void lookupEvent() {
        MyDBHandler dbHandler = new MyDBHandler(baseContext, null, null, 4);
        Event event = null;
        EditText et = null;

        isLoading = true;
        for (int i = 0; i < strHours.length; i++) {
            et = (EditText) baseActivity.findViewById(300 + i);

            if (et != null) {
                Log.d("event LOOKING FOR:", Integer.toString(et.getId()));
                event = dbHandler.findEvent(dateView.getText().toString(), et.getId());
                if (event != null) {
                    et.setText(String.valueOf(event.get_description()));
                } else {
                    et.setText(null);
                }
            }
        }
        isLoading = false;
    }

    public void newEvent(EditText et) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 4);
        Log.d("event SAVING:", Integer.toString(et.getId()));
        //Event event = new Event(dateView.getText().toString(), et.getText().toString(), et.getId());
        //Log.d("event SAVING:", Integer.toString(view.getId() - 300));
        dbHandler.addEvent(et.getId(), et.getText().toString(), dateView.getText().toString());
    }

    public void removeEvent(EditText et) {
        MyDBHandler dbHandler = new MyDBHandler(this, null,
                null, 4);
        Log.d("event DELETING:", Integer.toString(et.getId()));
        dbHandler.deleteEvent(dateView.getText().toString(), et.getId());
    }
}
