package com.mariotti.developer.futureclock.models.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mariotti.developer.futureclock.models.Alarm;

import java.util.UUID;

import static com.mariotti.developer.futureclock.models.WeekDay.FRIDAY;
import static com.mariotti.developer.futureclock.models.WeekDay.MONDAY;
import static com.mariotti.developer.futureclock.models.WeekDay.SATURDAY;
import static com.mariotti.developer.futureclock.models.WeekDay.SUNDAY;
import static com.mariotti.developer.futureclock.models.WeekDay.THURSDAY;
import static com.mariotti.developer.futureclock.models.WeekDay.TUESDAY;
import static com.mariotti.developer.futureclock.models.WeekDay.WEDNESDAY;
import static com.mariotti.developer.futureclock.models.WeekDay.reorderDays;
import static com.mariotti.developer.futureclock.models.database.AlarmDbSchema.*;

public class AlarmCursorWrapper extends CursorWrapper {

    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm() {
        String uuidString = getString(getColumnIndex(AlarmTable.Cols.UUID));
        String timeString = getString(getColumnIndex(AlarmTable.Cols.TIME));
        String[] timeSplit = timeString.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        int[] days = new int[7];
        addDay(days, AlarmTable.Cols.SUNDAY, SUNDAY);
        addDay(days, AlarmTable.Cols.MONDAY, MONDAY);
        addDay(days, AlarmTable.Cols.TUESDAY, TUESDAY);
        addDay(days, AlarmTable.Cols.WEDNESDAY, WEDNESDAY);
        addDay(days, AlarmTable.Cols.THURSDAY, THURSDAY);
        addDay(days, AlarmTable.Cols.FRIDAY, FRIDAY);
        addDay(days, AlarmTable.Cols.SATURDAY, SATURDAY);
        int[] daysReordered = reorderDays(days);

        String timezone = getString(getColumnIndex(AlarmTable.Cols.TIMEZONE));
        int active = getInt(getColumnIndex(AlarmTable.Cols.ACTIVE));

        Alarm alarm = new Alarm(
                UUID.fromString(uuidString),
                hour, minute,
                daysReordered,
                timezone,
                active == 1
        );

        return alarm;
    }

    private void addDay(int[] days, String columnName, int day) {
        if (getInt(getColumnIndex(columnName)) == 1) {
            days[day - 1] = day;
        } else {
            days[day - 1] = -1;
        }
    }
}