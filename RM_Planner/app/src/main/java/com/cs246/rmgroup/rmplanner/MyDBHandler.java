package com.cs246.rmgroup.rmplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robert on 6/27/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RM_Planner.db";
    private static final String TABLE_EVENTS = "events";
    private static final String EVENT_ID = "_id";
    private static final String EVENT_DESCRIPTION = "description";
    private static final String TABLE_GOALS = "goals";
    private static final String GOAL_ID = "_id";
    private static final String GOAL_DESCRIPTION = "description";
    private static final String TABLE_NOTES = "notes";
    private static final String NOTES_ID = "_id";
    private static final String NOTES_DESCRIPTION = "description";
    private static final String TABLE_CURRENTDATE = "currentdate";


    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_EVENTS + "("
                + EVENT_ID + " INTEGER PRIMARY KEY," + EVENT_DESCRIPTION
                + " TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
