package com.cs246.rmgroup.testnotes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView idView;
    TextView dayView;
    TextView descriptionView;
    static EditText dayBox;
    EditText descriptionBox;
    static TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idView = (TextView) findViewById(R.id.noteID);
        dayView = (TextView) findViewById(R.id.noteDay);
        descriptionView = (TextView) findViewById(R.id.noteDescription);
        dayBox = (EditText) findViewById(R.id.day);
        descriptionBox =
                (EditText) findViewById(R.id.description);
        dateView = (TextView) findViewById(R.id.dateView);
        Calendar thisDay = Calendar.getInstance();
        dateView.setText(getString(R.string.date_format,
                new DateFormatSymbols().getMonths()[thisDay.get(Calendar.MONTH)],
                thisDay.get(Calendar.DAY_OF_MONTH),
                thisDay.get(Calendar.YEAR)));
        dayBox.setText(getString(R.string.date_format,
                new DateFormatSymbols().getMonths()[thisDay.get(Calendar.MONTH)],
                thisDay.get(Calendar.DAY_OF_MONTH),
                thisDay.get(Calendar.YEAR)));
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
            dayBox.setText(getString(R.string.date_format,
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


    public void newNote (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //int quantity =
                //Integer.parseInt(quantityBox.getText().toString());

        Note note = new Note (dayBox.getText().toString(), descriptionBox.getText().toString());

        dbHandler.addNote(note);
        dayBox.setText("");
        descriptionBox.setText("");
    }

    public void lookupNote (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        Note note = dbHandler.findNote(dayBox.getText().toString());

        if (note != null) {
            idView.setText(String.valueOf(note.getID()));
            dayView.setText(String.valueOf(note.get_day()));
            descriptionView.setText(String.valueOf(note.get_description()));
            descriptionBox.setText(String.valueOf(note.get_description()));
        } else {
            idView.setText("No Match Found");
        }
    }

    public void removeNote (View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null,
                null, 1);

        boolean result = dbHandler.deleteNote(
                dayBox.getText().toString());

        if (result)
        {
            idView.setText("Record Deleted");
            dayBox.setText("");
            descriptionBox.setText("");
        }
        else
            idView.setText("No Match Found");
    }
}
