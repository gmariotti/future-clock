package com.mariotti.developer.futureclock.models

import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import org.junit.Assert
import org.junit.Test
import java.util.*

class AlarmTest {

	@Test fun getCorrectDatabaseAlarm() {
		val alarm = Alarm()
		val dbAlarmExpected = DatabaseAlarm(
				alarm.uuid, alarm.getHour(), alarm.getMinute(),
				alarm.days, alarm.getTimeZone(), alarm.active)

		Assert.assertEquals(dbAlarmExpected, Alarm.getDatabaseAlarm(alarm))
	}

	@Test fun getCorrectAlarm() {
		val dbAlarm = DatabaseAlarm(hour = 10, minute = 39)
		val time = HourMinuteAndTimeZone.getHourMinuteAndTimeZone(
				10, 39, TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
		val alarmExpected = Alarm(dbAlarm.uuid, time, dbAlarm.days, dbAlarm.active)

		Assert.assertEquals(alarmExpected, Alarm.getAlarm(dbAlarm))
	}
}