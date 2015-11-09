package com.mariotti.developer.futureclock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mariotti.developer.futureclock.database.AlarmDbSchema.AlarmTable;

/**
 * Is the class that manages the database
 */
public class AlarmBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "alarmBase.db";

    public AlarmBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AlarmTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        AlarmTable.Cols.UUID + ", " +
                        AlarmTable.Cols.TIME + ", " +
                        AlarmTable.Cols.MONDAY + ", " +
                        AlarmTable.Cols.TUESDAY + ", " +
                        AlarmTable.Cols.WEDNESDAY + ", " +
                        AlarmTable.Cols.THURSDAY + ", " +
                        AlarmTable.Cols.FRIDAY + ", " +
                        AlarmTable.Cols.SATURDAY + ", " +
                        AlarmTable.Cols.SUNDAY + ", " +
                        AlarmTable.Cols.ACTIVE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO -> manage the possibility to update the database to a new version
    }
}
