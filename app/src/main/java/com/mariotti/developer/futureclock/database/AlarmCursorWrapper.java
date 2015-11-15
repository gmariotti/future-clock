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
        addDay(days, AlarmTable.Cols.MONDAY, WeekDay.MONDAY);
        addDay(days, AlarmTable.Cols.TUESDAY, WeekDay.TUESDAY);
        addDay(days, AlarmTable.Cols.WEDNESDAY, WeekDay.WEDNESDAY);
        addDay(days, AlarmTable.Cols.THURSDAY, WeekDay.THURSDAY);
        addDay(days, AlarmTable.Cols.FRIDAY, WeekDay.FRIDAY);
        addDay(days, AlarmTable.Cols.SATURDAY, WeekDay.SATURDAY);
        addDay(days, AlarmTable.Cols.SUNDAY, WeekDay.SUNDAY);

        int active = getInt(getColumnIndex(AlarmTable.Cols.ACTIVE));

        Alarm alarm = new Alarm(
                UUID.fromString(uuidString),
                hour, minute,
                days,
                active == 1
        );

        return alarm;
    }

    private void addDay(EnumSet<WeekDay> days, String columnName, WeekDay day) {
        if (getInt(getColumnIndex(columnName)) == 1) {
            days.add(day);
        }
    }
}
