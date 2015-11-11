package com.mariotti.developer.futureclock.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mariotti.developer.futureclock.database.AlarmBaseHelper;
import com.mariotti.developer.futureclock.database.AlarmCursorWrapper;
import com.mariotti.developer.futureclock.database.AlarmDbSchema;
import com.mariotti.developer.futureclock.database.AlarmDbSchema.AlarmTable;
import com.mariotti.developer.futureclock.model.Alarm;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * Class that acts as a controller between the Alarm object and the Alarm database.
 * Singleton pattern
 */
public class AlarmController {
    private static AlarmController sAlarmController;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private AlarmController(Context context) {
        // TODO -> why application context ??
        mContext = context.getApplicationContext();
        mDatabase = new AlarmBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static AlarmController getAlarmController(Context context) {
        if (sAlarmController == null) {
            sAlarmController = new AlarmController(context);
        }
        return sAlarmController;
    }

    private static ContentValues getContentValues(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(AlarmTable.Cols.UUID, alarm.getUUID().toString());
        String time = alarm.getHour() + ":" + alarm.getMinute();
        values.put(AlarmTable.Cols.TIME, time);
        values.put(AlarmTable.Cols.MONDAY, alarm.hasMonday() ? 1 : 0);
        values.put(AlarmTable.Cols.TUESDAY, alarm.hasTuesday() ? 1 : 0);
        values.put(AlarmTable.Cols.WEDNESDAY, alarm.hasWednesday() ? 1 : 0);
        values.put(AlarmTable.Cols.THURSDAY, alarm.hasThursday() ? 1 : 0);
        values.put(AlarmTable.Cols.FRIDAY, alarm.hasFriday() ? 1 : 0);
        values.put(AlarmTable.Cols.SATURDAY, alarm.hasSaturday() ? 1 : 0);
        values.put(AlarmTable.Cols.SUNDAY, alarm.hasSunday() ? 1 : 0);
        values.put(AlarmTable.Cols.ACTIVE, alarm.isActive() ? 1 : 0);
        return values;
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

    public List<Alarm> getAlarms() {
        List<Alarm> alarms = new ArrayList<>();

        AlarmCursorWrapper cursorWrapper = queryAlarms(null, null);

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

    public void addAlarm(Alarm alarm) {
        ContentValues values = getContentValues(alarm);

        mDatabase.insert(AlarmTable.NAME, null, values);
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

    public void updateAlarm(Alarm alarm) {
        String uuidString = alarm.getUUID().toString();
        ContentValues values = getContentValues(alarm);

        mDatabase.update(
                AlarmTable.NAME,
                values,
                AlarmTable.Cols.UUID + " = ?",
                new String[]{uuidString}
        );
    }
}
