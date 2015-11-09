package com.mariotti.developer.futureclock.model;

/**
 * Enum class to represent the day of the week
 * TODO -> evaluate impact of enum on performance instead of using static final int
 */
public enum WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    public String getShortString() {
        switch (this) {
            case MONDAY:
                return "Mon";
            case TUESDAY:
                return "Tue";
            case WEDNESDAY:
                return "Wed";
            case THURSDAY:
                return "Thu";
            case FRIDAY:
                return "Fri";
            case SATURDAY:
                return "Sat";
            case SUNDAY:
                return "Sun";
            default:
                return "NoDay";
        }
    }
}
