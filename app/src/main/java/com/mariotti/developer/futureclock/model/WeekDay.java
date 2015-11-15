package com.mariotti.developer.futureclock.model;

import java.io.Serializable;

/**
 * Enum class to represent the day of the week
 * The integer value is the same of the java.util.Calendar class
 * TODO -> evaluate impact of enum on performance instead of using static final int
 */
public enum WeekDay implements Serializable {
    SUNDAY("Sun", 1),
    MONDAY("Mon", 2),
    TUESDAY("Tue", 3),
    WEDNESDAY("Wed", 4),
    THURSDAY("Thu", 5),
    FRIDAY("Fri", 6),
    SATURDAY("Sat", 7);

    private String shortName;
    private int calendarValue;

    private WeekDay(String shortName, int calendarValue) {
        this.shortName = shortName;
        this.calendarValue = calendarValue;
    }

    public String getShortName() {
        return shortName;
    }

    public int getCalendarValue() {
        return calendarValue;
    }
}
