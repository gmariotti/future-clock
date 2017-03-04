@file:JvmName("WeekDayTest")

package com.mariotti.developer.futureclock.models

import com.mariotti.developer.futureclock.models.WeekDay.getName
import com.mariotti.developer.futureclock.models.WeekDay.getShortName
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class WeekDayTest {

    @Test fun checkShortNameTest() {
        val days: IntArray = WeekDay.WEEK
        val shortDayNamesExpected: List<String> = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        days.forEachIndexed { index, day ->
            assertEquals(shortDayNamesExpected[index], getShortName(day))
        }
    }

    @Test fun checkNameTest() {
        val days = WeekDay.WEEK
        val dayNamesExpected = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        days.forEachIndexed { index, day ->
            assertEquals(dayNamesExpected[index], getName(day))
        }
    }

    @Test fun reorderDayTest() {
        val days = intArrayOf(1, 5, 9, 9, -3, 3, 3, 2, 7, 110, -11)

        val expected = intArrayOf(1, 2, 3, 5, 7)

        assertArrayEquals(expected, WeekDay.reorderDays(days))
    }
}
