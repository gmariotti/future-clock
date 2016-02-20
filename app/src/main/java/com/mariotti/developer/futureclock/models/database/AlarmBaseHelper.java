package com.mariotti.developer.futureclock.models.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "alarmBase.db";

    public AlarmBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AlarmDbSchema.AlarmTable.NAME + "(" +
                        "_id integer primary key autoincrement, " +
                        AlarmDbSchema.AlarmTable.Cols.UUID + ", " +
                        AlarmDbSchema.AlarmTable.Cols.TIME + ", " +
                        AlarmDbSchema.AlarmTable.Cols.MONDAY + ", " +
                        AlarmDbSchema.AlarmTable.Cols.TUESDAY + ", " +
                        AlarmDbSchema.AlarmTable.Cols.WEDNESDAY + ", " +
                        AlarmDbSchema.AlarmTable.Cols.THURSDAY + ", " +
                        AlarmDbSchema.AlarmTable.Cols.FRIDAY + ", " +
                        AlarmDbSchema.AlarmTable.Cols.SATURDAY + ", " +
                        AlarmDbSchema.AlarmTable.Cols.SUNDAY + ", " +
                        AlarmDbSchema.AlarmTable.Cols.ACTIVE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO -> manage the possibility to update the database to a new version
    }
}