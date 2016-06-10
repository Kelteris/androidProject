package com.cs246.rmgroup.rmplanner;

/**
 * Created by Asa on 6/6/2016.
 */
public class Event implements TableCreator {
    String _name;
    Date _time;

    public Event() {
        _name = "default";
        _time = new Date();

    }
    @Override
    public void setDate(int year, int month, int day, int hour, int minute, boolean am) {

    }

    @Override
    public void store() {

    }
}
