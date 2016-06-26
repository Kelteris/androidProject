package com.cs246.rmgroup.rmplanner;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Asa on 6/24/2016.
 * Stores log strings into file debuggedInfo.txt
 */
public class Logging extends Activity {
    private volatile static Logging instance = null;
    FileOutputStream outputStream;
    String filename = "debuggedInfo.txt";

    public static Logging getInstance() {
        if(instance == null) {
            Log.i("insert Log", "The instance is null");
            synchronized (Logging.class) {
                if(instance == null) {
                    instance = new Logging();
                    Log.i("insert Log", "The instance is no longer null");
                }
            }
        }
        return instance;
    }

    private Logging() {

    }

    public void insertLog(String string) {
        try {
            outputStream = getApplicationContext().openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e("insert Log", e.getMessage());
        }
    }
}
