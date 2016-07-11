package com.cs246.rmgroup.testnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robert on 7/2/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RM_Planner.db";
    private static final String TABLE_NOTE = "note";
    private static final String NOTE_ID = "_id";
    private static final String NOTE_DAY = "day";
    private static final String NOTE_DESCRIPTION = "description";

    /*private static final String DATABASE_NAME = "productDB.db";
    private static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";
    public static final String COLUMN_QUANTITY = "quantity";*/


    public MyDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTE_TABLE = "CREATE TABLE " +
                TABLE_NOTE + "("
                + NOTE_ID + " INTEGER PRIMARY KEY," + NOTE_DAY
                + " TEXT," + NOTE_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_NOTE_TABLE);
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

    public Note findNote(String currentDay, String description) {
        String query = "Select * FROM " + TABLE_NOTE + " WHERE " + NOTE_DAY + " =  \"" + currentDay + "\"" + " AND " + NOTE_DESCRIPTION + " =  \"" + description + "\"";

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

    /*public boolean deleteProduct(String productname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product product = new Product();

        if (cursor.moveToFirst()) {
            product.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(product.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }*/
}
