package com.cs246.rmgroup.rmplanner;

/**
 * Created by Asa on 6/6/2016.
 * Yeah, me. I made it
 */
public class Date implements TableCreator {
    int _year;
    int _month;
    int _day;
    int _hour;
    int _minute;
    boolean _am; // if true AM, false PM

    public Date() {
        _year = 2016;
        _month = 6;
        _day = 6;
        _hour = 14;
        _minute = 21;
        _am = false;
    }

    @Override
    public void store() {

    }

    @Override
    public void setDate(int year, int month, int day, int hour, int minute, boolean am) {
        _year = year;
        _month = month;
        _day = day;
        _hour = hour;
        _minute = minute;
        _am = am;

    }
}
