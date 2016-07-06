package com.cs246.rmgroup.rmplanner;

/**
 * Created by Robert on 6/6/2016.
 */
public class Goal {

    private int _id;
    private String _day;
    private String _description;

    public Goal() {
    }

    public Goal(String _day, String _description) {
        this._day = _day;
        this._description = _description;
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
}
