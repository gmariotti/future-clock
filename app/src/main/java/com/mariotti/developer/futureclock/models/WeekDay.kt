@file:JvmName("WeekDay")

package com.mariotti.developer.futureclock.models

import java.util.Calendar

object WeekDay {
    val SUNDAY = Calendar.SUNDAY
    val MONDAY = Calendar.MONDAY
    val TUESDAY = Calendar.TUESDAY
    val WEDNESDAY = Calendar.WEDNESDAY
    val THURSDAY = Calendar.THURSDAY
    val FRIDAY = Calendar.FRIDAY
    val SATURDAY = Calendar.SATURDAY
    val WEEK: IntArray = intArrayOf(SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY)


    // TODO -> return string based on system language
    fun getShortName(day: Int): String = when (day) {
        SUNDAY -> "Sun"
        MONDAY -> "Mon"
        TUESDAY -> "Tue"
        WEDNESDAY -> "Wed"
        THURSDAY -> "Thu"
        FRIDAY -> "Fri"
        SATURDAY -> "Sat"
        else -> "NoDay"
    }

    // TODO -> return string based on system language
    fun getName(day: Int): String = when (day) {
        SUNDAY -> "Sunday"
        MONDAY -> "Monday"
        TUESDAY -> "Tuesday"
        WEDNESDAY -> "Wednesday"
        THURSDAY -> "Thursday"
        FRIDAY -> "Friday"
        SATURDAY -> "Saturday"
        else -> "NoDay"
    }

    fun reorderDays(days: IntArray): IntArray = days.asSequence()
            .filter { it >= 1 && it <= 7 }
            .toSortedSet()
            .toIntArray()
}