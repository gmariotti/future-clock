package com.mariotti.developer.futureclock.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.WeekDay;
import com.mariotti.developer.futureclock.models.database.AlarmBaseHelper;
import com.mariotti.developer.futureclock.models.database.AlarmCursorWrapper;
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema;
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema.AlarmTable;
import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseAlarmController {
    private static final String TAG = "DatabaseAlarmController";
    private static DatabaseAlarmController sDatabaseAlarmController;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private DatabaseAlarmController(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AlarmBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static DatabaseAlarmController getDatabaseAlarmController(Context context) {
        if (sDatabaseAlarmController == null) {
            sDatabaseAlarmController = new DatabaseAlarmController(context);
        }
        return sDatabaseAlarmController;
    }

    public void addAlarm(Alarm alarm) {
        ContentValues values = getContentValues(alarm);

        mDatabase.insert(AlarmDbSchema.AlarmTable.NAME, null, values);
    }

    public Alarm getAlarm(UUID id) {
        AlarmCursorWrapper cursorWrapper = queryAlarms(
                AlarmDbSchema.AlarmTable.Cols.UUID + " = ?",
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
        if (alarm != null) {
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

    public int deleteAlarm(UUID uuid) {
        return mDatabase.delete(
                AlarmTable.NAME,
                AlarmTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()}
        );
    }

    public List<Alarm> getAlarms() {
        AlarmCursorWrapper cursorWrapper = queryAlarms(null, null);
        List<Alarm> alarms = createListFromCursorWrapper(cursorWrapper);

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

    private ContentValues getContentValues(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(AlarmTable.Cols.UUID, alarm.getUUID().toString());
        String time = alarm.getHour() + ":" + alarm.getMinute();
        values.put(AlarmTable.Cols.TIME, time);
        values.put(AlarmTable.Cols.MONDAY, AlarmUtil.hasDay(alarm, WeekDay.MONDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.TUESDAY, AlarmUtil.hasDay(alarm, WeekDay.TUESDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.WEDNESDAY, AlarmUtil.hasDay(alarm, WeekDay.WEDNESDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.THURSDAY, AlarmUtil.hasDay(alarm, WeekDay.THURSDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.FRIDAY, AlarmUtil.hasDay(alarm, WeekDay.FRIDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.SATURDAY, AlarmUtil.hasDay(alarm, WeekDay.SATURDAY) ? 1 : 0);
        values.put(AlarmTable.Cols.SUNDAY, AlarmUtil.hasDay(alarm, WeekDay.SUNDAY) ? 1 : 0);
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

}
