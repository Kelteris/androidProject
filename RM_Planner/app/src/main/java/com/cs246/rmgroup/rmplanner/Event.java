package com.cs246.rmgroup.rmplanner;

/**
 * Created by Asa on 6/6/2016.
 */
public class Event implements TableCreator {

    //private static final String TABLE_EVENTS = "events";
    private int _id;
    private String description;
    private Date _time;

    public Event(int _id, String description, Date _time) {
        this._id = _id;
        this.description = description;
        this._time = _time;
    }


    @Override
    public void setDate(int year, int month, int day, int hour, int minute, boolean am) {

    }

    @Override
    public void store() {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date get_time() {
        return _time;
    }

    public void set_time(Date _time) {
        this._time = _time;
    }
}
