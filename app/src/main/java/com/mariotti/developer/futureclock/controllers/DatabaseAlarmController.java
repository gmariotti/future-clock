package com.mariotti.developer.futureclock.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.WeekDay;
import com.mariotti.developer.futureclock.models.database.AlarmBaseHelper;
import com.mariotti.developer.futureclock.models.database.AlarmCursorWrapper;
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema.AlarmTable;
import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseAlarmController {
    private static DatabaseAlarmController sDatabaseAlarmController;

    private SQLiteDatabase mDatabase;

    private DatabaseAlarmController(Context context) {
        mDatabase = new AlarmBaseHelper(context.getApplicationContext())
                .getWritableDatabase();
    }

    public static DatabaseAlarmController getDatabaseAlarmController(Context context) {
        if (sDatabaseAlarmController == null) {
            sDatabaseAlarmController = new DatabaseAlarmController(context);
        }
        return sDatabaseAlarmController;
    }

    public void addAlarm(Alarm alarm) throws Exception {
        if (alarm != null) {
            ContentValues values = getContentValues(alarm);

            mDatabase.beginTransaction();
            try {
                long rowID = mDatabase.insert(AlarmTable.NAME, null, values);
                if (rowID == -1) {
                    throw new Exception("Error inserting alarm");
                }
                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    private ContentValues getContentValues(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(AlarmTable.Cols.UUID, alarm.getUuid().toString());
        String time = AlarmUtil.getHourAndMinuteAsString(alarm.getHour(), alarm.getMinute());
        values.put(AlarmTable.Cols.TIME, time);

        int days[] = alarm.getDays();
        values.put(AlarmTable.Cols.MONDAY, AlarmUtil.hasDay(days, WeekDay.MONDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.TUESDAY, AlarmUtil.hasDay(days, WeekDay.TUESDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.WEDNESDAY, AlarmUtil.hasDay(days, WeekDay.WEDNESDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.THURSDAY, AlarmUtil.hasDay(days, WeekDay.THURSDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.FRIDAY, AlarmUtil.hasDay(days, WeekDay.FRIDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.SATURDAY, AlarmUtil.hasDay(days, WeekDay.SATURDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.SUNDAY, AlarmUtil.hasDay(days, WeekDay.SUNDAY) ? 1 : 0);

        values.put(AlarmTable.Cols.TIMEZONE, alarm.getTimezone());
        values.put(AlarmTable.Cols.ACTIVE, alarm.getActive() ? 1 : 0);
        return values;
    }

    public Alarm getAlarm(UUID id) {
        AlarmCursorWrapper cursorWrapper = queryAlarms(
                AlarmTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getAlarm();
        } finally {
            cursorWrapper.close();
        }
    }

    private AlarmCursorWrapper queryAlarms(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                AlarmTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new AlarmCursorWrapper(cursor);
    }

    public void updateAlarm(Alarm alarm) throws Exception {
        if (alarm != null) {
            String uuidString = alarm.getUuid().toString();
            ContentValues values = getContentValues(alarm);

            mDatabase.beginTransaction();
            try {
                int numRowsModified = mDatabase.update(
                        AlarmTable.NAME,
                        values,
                        AlarmTable.Cols.UUID + " = ?",
                        new String[]{uuidString}
                );
                if (numRowsModified != 1) {
                    throw new Exception("Error updating alarm " + uuidString);
                }
                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }
        }
    }

    public void deleteAlarm(UUID uuid) throws Exception {
        mDatabase.beginTransaction();
        try {
            int numRowsDeleted = mDatabase.delete(
                    AlarmTable.NAME,
                    AlarmTable.Cols.UUID + " = ?",
                    new String[]{uuid.toString()}
            );
            if (numRowsDeleted != 1) {
                throw new Exception("Error delete alarm " + uuid.toString());
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    public List<Alarm> getAlarms() {
        AlarmCursorWrapper cursorWrapper = queryAlarms(null, null);
        List<Alarm> alarms = createListFromCursorWrapper(cursorWrapper);

        return alarms;
    }

    private List<Alarm> createListFromCursorWrapper(AlarmCursorWrapper cursorWrapper) {
        List<Alarm> alarms = new ArrayList<>();

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                alarms.add(cursorWrapper.getAlarm());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return alarms;
    }

    public List<Alarm> getActiveAlarms() {
        AlarmCursorWrapper cursorWrapper = queryAlarms(
                AlarmTable.Cols.ACTIVE + " = 1",
                null
        );
        List<Alarm> alarms = createListFromCursorWrapper(cursorWrapper);

        return alarms;
    }
}
