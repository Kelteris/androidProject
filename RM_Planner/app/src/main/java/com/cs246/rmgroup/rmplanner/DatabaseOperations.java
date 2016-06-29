package com.cs246.rmgroup.rmplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Robert on 6/14/2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper{

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE " +TableData.TableInfo.Table_Name+"("+TableData.TableInfo.description+" TEXT,"+TableData.TableInfo.time+"TEXT );";
    // Data base constructor
    public DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.Database_Name, null, database_version);
        Log.d("Database operations", "Database created");
    }



    @Override
    /**
     * constructor
     * @author Robert Dickerson
     * @param sdb
     */
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created");
    }

    @Override
    /**
     * onUpgrade updates the database to the new version
     * @author Robert Dickerson
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * stores informational strings with the time into a database
     * @author Robert Dickerson
     * @param dop
     * @param description
     * @param time
     */
    public void putInformation(DatabaseOperations dop, String description, String time) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.description, description);
        cv.put(TableData.TableInfo.time, time);
        long k = SQ.insert(TableData.TableInfo.Table_Name, null, cv);
        Log.d("Database operations", "One raw inserted");
    }
}
