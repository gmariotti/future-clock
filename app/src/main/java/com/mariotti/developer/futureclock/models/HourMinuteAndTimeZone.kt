package com.mariotti.developer.futureclock.models

import com.mariotti.developer.futureclock.extensions.getHourMinuteTimeZone
import java.util.*

data class HourMinuteAndTimeZone private constructor(val hour: Int, val minute: Int, val timezone: String) {

	companion object {
		fun getHourMinuteAndTimeZone(day: Calendar = Calendar.getInstance()): HourMinuteAndTimeZone {
			val (hour, minute, timezone) = day.getHourMinuteTimeZone(false)
			return HourMinuteAndTimeZone(hour, minute, timezone)
		}

		fun getHourMinuteAndTimeZone(hour: Int, minute: Int, timezone: String): HourMinuteAndTimeZone
				= HourMinuteAndTimeZone(hour % 24, minute % 60, timezone)
	}
}
