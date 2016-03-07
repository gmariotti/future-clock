@file:JvmName("AlarmUtilTest")

package com.mariotti.developer.futureclock.util

import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.WeekDay

import org.junit.Before
import org.junit.Test

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class AlarmUtilTest {
    var today: Calendar = Calendar.getInstance()
    var mAlarm: Alarm? = null

    @Before
    fun init() {
        mAlarm = Alarm(
                UUID.randomUUID(),
                today,
                intArrayOf(WeekDay.SUNDAY, WeekDay.MONDAY, WeekDay.THURSDAY, WeekDay.FRIDAY),
                false)
    }

    @Test
    fun alarmDaysStringTest() {
        val expected = StringBuilder()
        expected.append(WeekDay.getShortName(WeekDay.SUNDAY))
        expected.append(", ")
        expected.append(WeekDay.getShortName(WeekDay.MONDAY))
        expected.append(", ")
        expected.append(WeekDay.getShortName(WeekDay.THURSDAY))
        expected.append(", ")
        expected.append(WeekDay.getShortName(WeekDay.FRIDAY))

        assertEquals(expected.toString(), getShortDaysString(mAlarm!!))
    }

    @Test
    fun alarmTimeStringTest() {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        assertEquals(simpleDateFormat.format(today.time),
                getHourAndMinuteAsString(mAlarm!!.hour, mAlarm!!.minute))
    }

    @Test
    fun alarmAddDayTest() {
        val days = mAlarm!!.days
        val dayToAdd = WeekDay.WEDNESDAY
        val daysExpected = intArrayOf(WeekDay.SUNDAY, WeekDay.MONDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY)

        assertArrayEquals(daysExpected, addDay(days, dayToAdd))
    }

    @Test
    fun alarmRemoveDayTest() {
        val days = mAlarm!!.days
        val dayToRemove = WeekDay.SUNDAY
        val daysExpected = intArrayOf(WeekDay.MONDAY, WeekDay.THURSDAY, WeekDay.FRIDAY)

        assertArrayEquals(daysExpected, removeDay(days, dayToRemove))
    }

    @Test
    fun alarmContainsDayTest() {
        assertTrue(hasDay(mAlarm!!.days, WeekDay.FRIDAY))
    }
}
