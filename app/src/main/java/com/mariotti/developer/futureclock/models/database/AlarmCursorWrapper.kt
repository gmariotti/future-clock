package com.mariotti.developer.futureclock.models.database

import android.database.Cursor
import android.database.CursorWrapper
import android.util.Log
import com.mariotti.developer.futureclock.extensions.getIntFromColumnName
import com.mariotti.developer.futureclock.extensions.getStringFromColumnName
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.WeekDay
import com.mariotti.developer.futureclock.models.WeekDay.FRIDAY
import com.mariotti.developer.futureclock.models.WeekDay.MONDAY
import com.mariotti.developer.futureclock.models.WeekDay.SATURDAY
import com.mariotti.developer.futureclock.models.WeekDay.SUNDAY
import com.mariotti.developer.futureclock.models.WeekDay.THURSDAY
import com.mariotti.developer.futureclock.models.WeekDay.TUESDAY
import com.mariotti.developer.futureclock.models.WeekDay.WEDNESDAY
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema.AlarmTable
import java.util.*

class AlarmCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    fun getAlarmFromDb(): Alarm {
        val uuidString = getStringFromColumnName(AlarmTable.Cols.UUID)
        val timeString = getStringFromColumnName(AlarmTable.Cols.TIME)
        val timeSplit = timeString.split(":".toRegex(), limit = 2)
        val hour = Integer.parseInt(timeSplit[0])
        val minute = Integer.parseInt(timeSplit[1])

        val daysReordered = createDaysArray()

        val timezone = getStringFromColumnName(AlarmTable.Cols.TIMEZONE)
        val active = getIntFromColumnName(AlarmTable.Cols.ACTIVE)

        val alarm = Alarm(
                UUID.fromString(uuidString),
                hour, minute,
                daysReordered,
                timezone,
                active == 1
        )

        return alarm
    }

    private fun createDaysArray(): IntArray {
        val days = IntArray(7)

        // TODO - reconsider
        addDay(days, AlarmTable.Cols.SUNDAY, SUNDAY)
        addDay(days, AlarmTable.Cols.MONDAY, MONDAY)
        addDay(days, AlarmTable.Cols.TUESDAY, TUESDAY)
        addDay(days, AlarmTable.Cols.WEDNESDAY, WEDNESDAY)
        addDay(days, AlarmTable.Cols.THURSDAY, THURSDAY)
        addDay(days, AlarmTable.Cols.FRIDAY, FRIDAY)
        addDay(days, AlarmTable.Cols.SATURDAY, SATURDAY)

        return WeekDay.reorderDays(days)
    }

    private fun addDay(days: IntArray, columnName: String, day: Int) {
        if (getIntFromColumnName(columnName) == 1) {
            days[day - 1] = day
        } else {
            days[day - 1] = -1
        }
    }
}