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
        String uuidString = getString(getColumnIndex(AlarmDbSchema.AlarmTable.Cols.UUID));
        String timeString = getString(getColumnIndex(AlarmDbSchema.AlarmTable.Cols.TIME));
        String[] timeSplit = timeString.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        int[] days = new int[7];
        addDay(days, AlarmDbSchema.AlarmTable.Cols.SUNDAY, SUNDAY);
        addDay(days, AlarmDbSchema.AlarmTable.Cols.MONDAY, MONDAY);
        addDay(days, AlarmDbSchema.AlarmTable.Cols.TUESDAY, TUESDAY);
        addDay(days, AlarmDbSchema.AlarmTable.Cols.WEDNESDAY, WEDNESDAY);
        addDay(days, AlarmDbSchema.AlarmTable.Cols.THURSDAY, THURSDAY);
        addDay(days, AlarmDbSchema.AlarmTable.Cols.FRIDAY, FRIDAY);
        addDay(days, AlarmDbSchema.AlarmTable.Cols.SATURDAY, SATURDAY);
        int[] daysReordered = reorderDays(days);

        int active = getInt(getColumnIndex(AlarmDbSchema.AlarmTable.Cols.ACTIVE));

        Alarm alarm = new Alarm(
                UUID.fromString(uuidString),
                hour, minute,
                daysReordered,
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