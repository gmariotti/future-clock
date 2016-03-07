@file:JvmName("AlarmManagementController")

package com.mariotti.developer.futureclock.controllers

import com.mariotti.developer.futureclock.models.Alarm
import java.util.*

fun getNextAlarm(alarms: List<Alarm>): Alarm? {
    val today = Calendar.getInstance()
    val indexOfAlarm: Int = alarms.mapIndexed { index, alarm ->
        Pair(index, getNearestDayForAlarm(alarm, today))
    }
            .sortedBy { pair: Pair<Int, Calendar> -> pair.second }[0]
            .first

    return if (indexOfAlarm == 0) null else alarms[indexOfAlarm]
}

fun getNearestDayForAlarm(alarm: Alarm, day: Calendar): Calendar {
    val initializedDay = initializeHourMinuteAndTimezone(alarm, day)

    val days = alarm.days
    if (days.size == 0) {
        return initializedDay
    }

    var nearestDay = setCorrectDay(initializedDay, days[0])
    for (i in 1 until days.size) {
        val compareDay = setCorrectDay(initializedDay, days[i])
        if (nearestDay.after(compareDay)) {
            nearestDay = compareDay
        }
    }

    return nearestDay
}

private fun initializeHourMinuteAndTimezone(alarm: Alarm, day: Calendar): Calendar {
    val dayToReturn = day.clone() as Calendar
    initializeHourAndMinute(alarm, dayToReturn)
    initializeTimezone(alarm, dayToReturn)
    correctDay(day, dayToReturn)

    return dayToReturn
}

private fun initializeHourAndMinute(alarm: Alarm, dayToReturn: Calendar) {
    dayToReturn.set(Calendar.HOUR_OF_DAY, alarm.hour)
    dayToReturn.set(Calendar.MINUTE, alarm.minute)
}

private fun initializeTimezone(alarm: Alarm, dayToReturn: Calendar) {
    if (alarm.timezone != dayToReturn.timeZone.displayName) {
        // TODO - modify timezone accordingly
    }
}

private fun correctDay(day: Calendar, dayToReturn: Calendar) {
    if (dayToReturn.before(day)) {
        dayToReturn.set(Calendar.DAY_OF_YEAR, dayToReturn.get(Calendar.DAY_OF_YEAR) + 1)
    }
}

private fun setCorrectDay(day: Calendar, weekDay: Int): Calendar {
    val dayToReturn = day.clone() as Calendar
    dayToReturn.set(Calendar.DAY_OF_WEEK, weekDay)
    correctWeekDay(day, dayToReturn)

    return dayToReturn
}

private fun correctWeekDay(day: Calendar, dayToReturn: Calendar) {
    if (dayToReturn.before(day)) {
        dayToReturn.set(Calendar.WEEK_OF_YEAR, dayToReturn.get(Calendar.WEEK_OF_YEAR) + 1)
    }
}