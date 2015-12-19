package com.mariotti.developer.futureclock.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mariotti.developer.futureclock.database.AlarmBaseHelper;
import com.mariotti.developer.futureclock.database.AlarmCursorWrapper;
import com.mariotti.developer.futureclock.database.AlarmDbSchema;
import com.mariotti.developer.futureclock.database.AlarmDbSchema.AlarmTable;
import com.mariotti.developer.futureclock.model.Alarm;
import com.mariotti.developer.futureclock.model.WeekDay;
import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * Class that acts as a controller between the Alarm object and the Alarm database.
 * Singleton pattern
 */
public class AlarmController {
    private static final String TAG = "AlarmController";

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

    /**
     * Get the list of all alarms in the database
     *
     * @return list of alarms as an ArrayList
     */
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

    /**
     * Get the list of all alarms active in the database
     *
     * @return list of alarms active as an ArrayList
     */
    public List<Alarm> getActiveAlarms() {
        List<Alarm> alarms = new ArrayList<>();

        AlarmCursorWrapper cursorWrapper = queryAlarms(
                AlarmTable.Cols.ACTIVE + " = 1",
                null
        );

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

    /**
     * Add an alarm to the database
     *
     * @param alarm value to insert in the database
     */
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

    /**
     * Check the database for all enabled alarms and get it.
     * Then check the first alarm in time that will be fired
     *
     * @return the next alarm to fire, or null if not found
     */
    public Alarm getNextAlarm() {
        // get current day
        Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DAY_OF_WEEK);
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);

        Log.d(TAG, "Day: " + day + " time: " + hour + ":" + minute);

        // order the list of alarms in respect to the current day and time
        List<Alarm> alarms = getActiveAlarms();
        if (!alarms.isEmpty()) {
            Collections.sort(alarms, (lAlarm, rAlarm) -> {
                int lAlarmNearestDay = AlarmUtil.getNearestDay(lAlarm, day, hour, minute);
                int rAlarmNearestDay = AlarmUtil.getNearestDay(rAlarm, day, hour, minute);

                if (WeekDay.compare(lAlarmNearestDay, rAlarmNearestDay, day) == 0) {
                    if (lAlarm.getHour() < rAlarm.getHour()) {
                        return -1;
                    } else if (lAlarm.getHour() == rAlarm.getHour() && lAlarm.getMinute() < rAlarm.getMinute()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (WeekDay.compare(lAlarmNearestDay, rAlarmNearestDay, day) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            });
        } else {
            return null;
        }

        // correction in the sort. Move at the bottom of the list the alarms in the same day but time before today
        boolean notRotated = false;
        while (!notRotated && alarms.size() > 1) {
            notRotated = true;
            Alarm firstListAlarm = alarms.get(0);
            if (AlarmUtil.getNearestDay(firstListAlarm, day, hour, minute) == day) {
                if (firstListAlarm.getHour() < hour ||
                        (firstListAlarm.getHour() == hour && firstListAlarm.getMinute() <= minute)) {
                    notRotated = false;
                    Collections.rotate(alarms, -1);
                }
            }
        }

        // TODO -> just for debugging, if constant can be eliminated
        for (Alarm alarm : alarms) {
            Log.d(TAG, "alarm " + AlarmUtil.getNearestDay(alarm, day, hour, minute)
                    + " " + alarm.getTime());
        }

        return alarms.get(0);
    }

    /**
     * Update an alarm in the database
     *
     * @param alarm value to update
     */
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

    /**
     * Delete an alarm in the database based on its uuid
     *
     * @param uuid identifier of the alarm to delete
     * @return 1 if the row is deleted, 0 otherwise
     */
    public int deleteAlarm(UUID uuid) {
        return mDatabase.delete(
                AlarmTable.NAME,
                AlarmTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()}
        );
    }
}
