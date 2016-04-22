package com.mariotti.developer.futureclock.extensions

import java.util.*

fun Calendar.getHourMinuteTimeZone(timezoneLong: Boolean): Triple<Int, Int, String> {
	val timezone = if (timezoneLong) this.timeZone.getDisplayName(false, TimeZone.LONG)
					else this.timeZone.getDisplayName(false, TimeZone.SHORT)
	return Triple(this.get(Calendar.HOUR_OF_DAY), this.get(Calendar.MINUTE), timezone)
}

fun Calendar.setHourMinuteTimeZone(triple: Triple<Int, Int, String>): Calendar {
	val (hour, minute, timezone) = triple
	this.set(Calendar.HOUR_OF_DAY, hour % 24)
	this.set(Calendar.MINUTE, minute % 60)
	this.timeZone = TimeZone.getTimeZone(timezone)

	val value = Calendar.getInstance()
	value.time = this.time
	return value
}
