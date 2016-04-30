package com.mariotti.developer.futureclock.models

import com.mariotti.developer.futureclock.functions.getDefaultTimeZone
import com.mariotti.developer.futureclock.extensions.getHourMinuteTimeZone
import java.util.Calendar

data class HourMinuteAndTimeZone(val hour: Int, val minute: Int, val timezone: String) {

	companion object {
		fun getFromCalendar(day: Calendar = Calendar.getInstance()): HourMinuteAndTimeZone {
			val (hour, minute, timezone) = day.getHourMinuteTimeZone(false)
			return HourMinuteAndTimeZone(hour, minute, timezone)
		}

		fun getFromVariables(hour: Int, minute: Int, timezone: String = getDefaultTimeZone()):
						HourMinuteAndTimeZone = HourMinuteAndTimeZone(hour % 24, minute % 60, timezone)
	}
}

