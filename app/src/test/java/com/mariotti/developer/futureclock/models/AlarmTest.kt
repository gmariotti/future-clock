@file:JvmName(name = "AlarmTest")

package com.mariotti.developer.futureclock.models

import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmTest {

	@Test fun getCorrectDatabaseAlarm() {
		val alarm = Alarm()
		val dbAlarmExpected = DatabaseAlarm(
						alarm.uuid, alarm.getHour(), alarm.getMinute(),
						alarm.days, alarm.getTimeZone(), alarm.active)

		assertEquals(dbAlarmExpected, Alarm.getDatabaseAlarm(alarm))
	}

	@Test fun getCorrectAlarm() {
		val dbAlarm = DatabaseAlarm(hour = 10, minute = 39)
		val time = HourMinuteAndTimeZone.getFromVariables(10, 39)
		val alarmExpected = Alarm(dbAlarm.uuid, time, dbAlarm.days, dbAlarm.active)

		assertEquals(alarmExpected, Alarm.getAlarm(dbAlarm))
	}
}