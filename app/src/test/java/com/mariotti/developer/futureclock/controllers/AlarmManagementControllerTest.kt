package com.mariotti.developer.futureclock.controllers

import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.WeekDay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class AlarmManagementControllerTest {
    lateinit var testAlarms: List<Alarm>
    var today: Calendar = Calendar.getInstance()
    var simpleDateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy z")

    @Before
    fun init() {
        // it is 18:00 31/12/2016 - is a Saturday
        today.set(2016, Calendar.DECEMBER, 31, 18, 0)

        val timezone = today.timeZone.getDisplayName(false, TimeZone.SHORT)
//        val alarm1 = Alarm(UUID.randomUUID(), 18, 7, intArrayOf(WeekDay.MONDAY, WeekDay.FRIDAY),
//                timezone, true)
//        val alarm2 = Alarm(UUID.randomUUID(), 10, 34, intArrayOf(WeekDay.SATURDAY, WeekDay.SUNDAY),
//                timezone, true)
//        val alarm3 = Alarm(UUID.randomUUID(), 5, 11, intArrayOf(WeekDay.THURSDAY),
//                timezone, true)
//        val alarm4 = Alarm(UUID.randomUUID(), 22, 34, intArrayOf(), timezone, false)
//        testAlarms = listOf(alarm1, alarm2, alarm3, alarm4)
    }

    @Test
    fun getNextAlarmTest() {
        val activeTestAlarms = testAlarms.filter { it.active }

        val alarm = getNextAlarm(activeTestAlarms, today)
        val alarmExpected = testAlarms[1] // is alarm2 because of Sunday 10:34
        assertEquals(alarmExpected, alarm)
    }

    @Test
    fun getNextAlarmNotNullTest() {
        val alarm = getNextAlarm(testAlarms)
        assertNotNull(alarm)
    }

    @Test
    fun getNearestDayForAlarmNotNullTest() {
        for (alarm in testAlarms) {
            val day = getNearestDayForAlarm(alarm, today)
            assertNotNull(day)
            println(simpleDateFormat.format(day.time))
        }
    }

    @Test
    fun alarmWithDifferentTimezoneTest() {
        // TODO
    }
}
