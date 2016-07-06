package com.cs246.rmgroup.rmplanner;

/**
 * Created by Asa on 6/6/2016.
 */
public class Event {

    private int _id;
    private String _day;
    private String _description;
    private int _hour;

    public Event(String _day, String _description, int _hour) {
        this._day = _day;
        this._description = _description;
        this._hour = _hour;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_day() {
        return _day;
    }

    public void set_day(String _day) {
        this._day = _day;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public int get_hour() {
        return _hour;
    }

    public void set_hour(int _hour) {
        this._hour = _hour;
    }
}
