package com.cs246.rmgroup.rmplanner;

/**
 * Created by Asa on 6/6/2016.
 */
public class Event implements TableCreator {
    String _name;
    Date _time;

    public Event() {
        _name = "default";
        _time.setDate(0/*current year*/, 0/*current month*/, 0/*current day*/,0/*current hour*/,0/*current minute*/, false /*AM || PM*/);
    }

    @Override
    public void setDate(int year, int month, int day, int hour, int minute, boolean am) {

    }

    @Override
    public void store() {

    }
}
