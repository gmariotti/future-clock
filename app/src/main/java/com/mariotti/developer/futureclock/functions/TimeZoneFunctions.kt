package com.mariotti.developer.futureclock.functions

import java.util.*

fun getDefaultTimeZone(shortTimeZone: Boolean = true): String {
	var shortInt: Int =	if (shortTimeZone) TimeZone.SHORT else TimeZone.LONG
	return TimeZone.getDefault().getDisplayName(false, shortInt)
}