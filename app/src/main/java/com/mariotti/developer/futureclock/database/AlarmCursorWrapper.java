package com.mariotti.developer.futureclock.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mariotti.developer.futureclock.database.AlarmDbSchema.AlarmTable;
import com.mariotti.developer.futureclock.model.Alarm;
import com.mariotti.developer.futureclock.model.WeekDay;

import java.util.EnumSet;
import java.util.UUID;

public class AlarmCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm() {
        String uuidString = getString(getColumnIndex(AlarmTable.Cols.UUID));
        String timeString = getString(getColumnIndex(AlarmTable.Cols.TIME));
        String[] timeSplit = timeString.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        EnumSet<WeekDay> days = EnumSet.noneOf(WeekDay.class);
        if (getInt(getColumnIndex(AlarmTable.Cols.MONDAY)) == 1) {
            days.add(WeekDay.MONDAY);
        }
        if (getInt(getColumnIndex(AlarmTable.Cols.TUESDAY)) == 1) {
            days.add(WeekDay.TUESDAY);
        }
        if (getInt(getColumnIndex(AlarmTable.Cols.WEDNESDAY)) == 1) {
            days.add(WeekDay.WEDNESDAY);
        }
        if (getInt(getColumnIndex(AlarmTable.Cols.THURSDAY)) == 1) {
            days.add(WeekDay.THURSDAY);
        }
        if (getInt(getColumnIndex(AlarmTable.Cols.FRIDAY)) == 1) {
            days.add(WeekDay.FRIDAY);
        }
        if (getInt(getColumnIndex(AlarmTable.Cols.SATURDAY)) == 1) {
            days.add(WeekDay.SATURDAY);
        }
        if (getInt(getColumnIndex(AlarmTable.Cols.SUNDAY)) == 1) {
            days.add(WeekDay.MONDAY);
        }
        int active = getInt(getColumnIndex(AlarmTable.Cols.ACTIVE));

        Alarm alarm = new Alarm(
                UUID.fromString(uuidString),
                hour, minute,
                days,
                active == 1
        );

        return alarm;
    }
}
