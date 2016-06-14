package com.cs246.rmgroup.rmplanner;

import android.provider.BaseColumns;

/**
 * Created by Robert on 6/14/2016.
 */
public class TableData {

    public TableData () {

    }

    public static abstract class TableInfo implements BaseColumns {

        public static final String _description = "description";
        public static final String _databaseName = "activity";
        public static final String _tableName = "activityInfo";
    }

}
