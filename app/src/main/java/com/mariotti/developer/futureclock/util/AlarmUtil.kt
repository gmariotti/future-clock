@file:JvmName("AlarmUtil")

package com.mariotti.developer.futureclock.util

import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.WeekDay
import java.util.*

fun getDefaultAlarmInstance(): Alarm =
        Alarm(UUID.randomUUID(), Calendar.getInstance(), intArrayOf(), false)

fun getShortDaysString(alarm: Alarm): String {
    var result = StringBuilder()
    val days = alarm.days

    if (days.size > 0) {
        val maxIndex = days.lastIndex
        days.forEachIndexed {
            index, day ->
            result.append(WeekDay.getShortName(day))
            if (index < maxIndex) result.append(", ")
        }
    } else {
        result.append(WeekDay.getShortName(-1))
    }

    return result.toString()
}

fun getHourAndMinuteAsString(hour: Int, minute: Int): String {
    val hourString = if (hour < 10) "0$hour" else Integer.toString(hour)
    val minuteString = if (minute < 10) "0$minute" else Integer.toString(minute)
    return "$hourString:$minuteString"
}

fun addDay(days: IntArray, day: Int): IntArray {
    val newDays = days.toSortedSet()
    newDays.add(day)

    return newDays.toIntArray()
}

fun removeDay(days: IntArray, day: Int): IntArray = WeekDay.reorderDays(days.filter { it != day }.toIntArray())

fun hasDay(days: IntArray, day: Int): Boolean = days.contains(day)