package com.cs246.rmgroup.rmplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robert on 7/2/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "RM_Planner.db";
    private static final String TABLE_NOTE = "note";
    private static final String NOTE_ID = "_id";
    private static final String NOTE_DAY = "day";
    private static final String NOTE_DESCRIPTION = "description";
    private static final String TABLE_EVENT = "event";
    private static final String EVENT_ID = "_id";
    private static final String EVENT_DAY = "day";
    private static final String EVENT_DESCRIPTION = "description";
    private static final String EVENT_HOUR = "hour";

    /*private static final String DATABASE_NAME = "productDB.db";
    private static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";*/

    // Create the Note Table
    public static final String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
            + NOTE_ID + " INTEGER PRIMARY KEY," + NOTE_DAY
            + " TEXT," + NOTE_DESCRIPTION + " TEXT" + ")";

    // Create the Event Table
    public static final String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "("
            + EVENT_ID + " INTEGER PRIMARY KEY," + EVENT_DAY
            + " TEXT," + EVENT_DESCRIPTION + " TEXT,"
            + EVENT_HOUR + " INTEGER" + ")";



    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL(CREATE_NOTE_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);

        /*String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME
                + " TEXT," + COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        onCreate(db);
    }

    public void addNote(Note note) {

        ContentValues values = new ContentValues();
        values.put(NOTE_DAY, note.get_day());
        values.put(NOTE_DESCRIPTION, note.get_description());


        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NOTE, null, values);
        db.close();
    }


    /*public void addProduct(Product product) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_QUANTITY, product.getQuantity());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }*/

    public Note findNote(String currentDay) {
        String query = "Select * FROM " + TABLE_NOTE + " WHERE " + NOTE_DAY + " =  \"" + currentDay + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Note note = new Note();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            note.setID(Integer.parseInt(cursor.getString(0)));
            note.set_day(cursor.getString(1));
            note.set_description(cursor.getString(2));
            cursor.close();
        } else {
            note = null;
        }
        db.close();
        return note;
    }


    /*public Product findProduct(String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product product = new Product();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setQuantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }*/

    public boolean deleteNote(String currentDay) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_NOTE + " WHERE " + NOTE_DAY + " =  \"" + currentDay + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Note note = new Note();

        if (cursor.moveToFirst()) {
            note.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NOTE, NOTE_ID + " = ?",
                    new String[] { String.valueOf(note.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void addEvent(int hour, String description, String day) {

        ContentValues values = new ContentValues();
        values.put(EVENT_HOUR, hour);
        values.put(EVENT_DESCRIPTION, description);
        values.put(EVENT_DAY, day);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EVENT, null, values);
        db.close();
    }

    public Event findEvent(String currentDay, int currentHour) {
        String query = "Select * FROM " + TABLE_EVENT + " WHERE " + EVENT_DAY + " =  \"" + currentDay + "\"" + " AND " + EVENT_HOUR + " = \"" + currentHour + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Event event = new Event();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            event.set_id(Integer.parseInt(cursor.getString(0)));
            event.set_day(cursor.getString(1));
            event.set_description(cursor.getString(2));
            event.set_hour(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        } else {
            event = null;
        }
        db.close();
        return event;
    }

    public boolean deleteEvent(String currentDay, int currentHour) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_EVENT + " WHERE "
                + EVENT_DAY + " =  \"" + currentDay + "\"" + " AND "
                + EVENT_HOUR + " = \"" + currentHour + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Event event = new Event();

        if (cursor.moveToFirst()) {
            event.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_EVENT, EVENT_ID + " = ?",
                    new String[] { String.valueOf(event.get_id()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
