package com.cs246.rmgroup.rmplanner;

/**
 * Created by Robert on 6/29/2016.
 */
public class Note {

    private int _id;
    private String descirption = "add a note";
    private String day= "";

    public Note() {
    }

    public Note(int _id, String descirption) {
        this._id = _id;
        this.descirption = descirption;
    }

    public Note(int _id, String descirption, String date) {
        this._id = _id;
        this.descirption = descirption;
        this.day = day;
    }

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public String getDescirption() {
        return descirption;
    }

    public void setDescirption(String descirption) {
        this.descirption = descirption;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
