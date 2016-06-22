package com.cs246.rmgroup.rmplanner;

import android.provider.BaseColumns;

/**
 * Created by Robert on 6/14/2016.
 */
public class TableData {

    public TableData () {

    }

    public static abstract class TableInfo implements BaseColumns {

        public static final String description = "description";
        public static final String time = "time";
        public static final String Database_Name = "activity";
        public static final String Table_Name = "activityInfo";
    }

}
