package com.cs246.rmgroup.rmplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robert on 6/27/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RM_Planner.db";
    private static final String TABLE_EVENT = "event";
    private static final String EVENT_ID = "_id";
    private static final String EVENT_DESCRIPTION = "description";
    private static final String TABLE_GOAL = "goal";
    private static final String GOAL_ID = "_id";
    private static final String GOAL_DESCRIPTION = "description";
    private static final String TABLE_NOTE = "note";
    private static final String NOTE_ID = "_id";
    private static final String NOTE_DESCRIPTION = "description";
    private static final String NOTE_DAY = "2015-01-22";
    //private static final String TABLE_DAY = "day";


    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENT_TABLE = "CREATE TABLE " +
                TABLE_EVENT + "("
                + EVENT_ID + " INTEGER PRIMARY KEY," + EVENT_DESCRIPTION
                + " TEXT)";
        db.execSQL(CREATE_EVENT_TABLE);

        String CREATE_GOAL_TABLE = "CREATE TABLE " +
                TABLE_GOAL + "("
                + GOAL_ID + " INTEGER PRIMARY KEY," + GOAL_DESCRIPTION
                + " TEXT)";
        db.execSQL(CREATE_GOAL_TABLE);

        String CREATE_NOTE_TABLE = "CREATE TABLE " +
                TABLE_NOTE + "("
                + NOTE_ID + " INTEGER PRIMARY KEY," + NOTE_DESCRIPTION
                + " TEXT," + NOTE_DAY + " TEXT)";
        db.execSQL(CREATE_NOTE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY);
        onCreate(db);


    }

    public void addNote(Note note) {

        ContentValues values = new ContentValues();
        values.put(NOTE_DESCRIPTION, note.getDescirption());
        values.put(NOTE_DAY, note.getDay());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NOTE, null, values);
        db.close();
    }

    //The date() function returns the date in this format: YYYY-MM-DD.
    public Note findNote(String productname) {
        String query = "Select * FROM " + TABLE_NOTE + " WHERE " + "add a day function here so the we remember that each note will have a day it is linked too and must be linked this way." + " =  \"" + productname  + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Note note= new Note();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            note.setID(Integer.parseInt(cursor.getString(0)));
            note.setDescirption(cursor.getString(1));
            note.setDay(cursor.getString(2));
            cursor.close();
        } else {
            note = null;
        }
        db.close();
        return note;
    }

    /* pass in the id and the current string content to know if the string id is empty and should be deleted */
    public boolean deleteNote(String productname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_NOTE + " WHERE "/* + COLUMN_PRODUCTNAME + " =  \"" + productname + "\""*/;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Note note = new Note();

        if (cursor.moveToFirst()) {
            //note.set_id(Integer.parseInt(cursor.getString(0)));
            //db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                  //  new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
