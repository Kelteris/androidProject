package com.cs246.rmgroup.rmplanner;

/**
 * Created by Asa on 6/6/2016.
 */
public interface TableCreator {
    public void store();
    public void setDate(int year, int month, int day, int hour, int minute, boolean am);
}
