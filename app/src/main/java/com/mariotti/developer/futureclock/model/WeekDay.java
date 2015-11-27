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

    public static WeekDay next(WeekDay day) {
        switch (day) {
            case MONDAY:
                return TUESDAY;
            case TUESDAY:
                return WEDNESDAY;
            case WEDNESDAY:
                return THURSDAY;
            case THURSDAY:
                return FRIDAY;
            case FRIDAY:
                return SATURDAY;
            case SATURDAY:
                return SUNDAY;
            case SUNDAY:
                return MONDAY;
            default:
                // should never happen
                return null;
        }
    }

    public static WeekDay getFromInt(int day) {
        switch (day) {
            case 1:
                return SUNDAY;
            case 2:
                return MONDAY;
            case 3:
                return TUESDAY;
            case 4:
                return WEDNESDAY;
            case 5:
                return THURSDAY;
            case 6:
                return FRIDAY;
            case 7:
                return SATURDAY;
            default:
                return null;
        }
    }

    public int compare(WeekDay dayToCompare, WeekDay dayCenter) {
        // case in which this is a day before dayToCompare
        if (this.getCalendarValue() < dayToCompare.getCalendarValue()) {
            if (this.getCalendarValue() >= dayCenter.getCalendarValue()) {
                return -1;
            } else {
                if (dayToCompare.getCalendarValue() >= dayCenter.getCalendarValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        } else if (this.getCalendarValue() > dayToCompare.getCalendarValue()) {
            // case in which this is after dayToCompare
            if (dayToCompare.getCalendarValue() >= dayCenter.getCalendarValue()) {
                return 1;
            } else {
                if (this.getCalendarValue() >= dayCenter.getCalendarValue()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            return 0;
        }
    }

    public int getDayDifference(WeekDay dayToCompare) {
        // it means that this day is before the one to compare, so will occur in the next week
        if (this.getCalendarValue() < dayToCompare.getCalendarValue()) {
            return 7 - dayToCompare.getCalendarValue() + this.getCalendarValue();
        }
        // it means that this day is after the one to compare, so is in the same week
        else {
            return this.getCalendarValue() - dayToCompare.getCalendarValue();
        }
    }
}
