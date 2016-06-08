package com.cs246.rmgroup.rmplanner;

/**
 * Created by Robert on 6/6/2016.
 */
public class Goal implements TableCreator {
    String _name;
    Date _time;

    public Goal() {
        _name = "Goal name should be put here";
        _time.setDate(0/*current year*/, 0/*current month*/, 0/*current day*/,0/*current hour*/,0/*current minute*/, false /*AM || PM*/);
    }
    @Override
    public void store() {

    }

    @Override
    public void setDate(int year, int month, int day, int hour, int minute, boolean am) {

    }
}
