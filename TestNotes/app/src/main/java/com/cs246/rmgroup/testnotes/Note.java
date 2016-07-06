package com.cs246.rmgroup.testnotes;

/**
 * Created by Robert on 7/2/2016.
 */
public class Note {

    private int _id;
    private String _day;
    private String _description;


    public Note() {
    }

    public Note(String _day, String _description) {
        this._day = _day;
        this._description = _description;

    }

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
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
