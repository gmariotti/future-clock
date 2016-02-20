package com.mariotti.developer.futureclock.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Represent the days of the week, considering Sunday as the first day of the week.
 * Day's value is equal to the value of the Calendar class
 */
public class WeekDay {
    public static final int SUNDAY = Calendar.SUNDAY;
    public static final int MONDAY = Calendar.MONDAY;
    public static final int TUESDAY = Calendar.TUESDAY;
    public static final int WEDNESDAY = Calendar.WEDNESDAY;
    public static final int THURSDAY = Calendar.THURSDAY;
    public static final int FRIDAY = Calendar.FRIDAY;
    public static final int SATURDAY = Calendar.SATURDAY;

    // TODO -> return string based on system language
    public static String getShortName(int day) {
        switch (day) {
            case SUNDAY:
                return "Sun";
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
            default:
                return "NoDay";
        }
    }

    // TODO -> return string based on system language
    public static String getName(int day) {
        switch (day) {
            case SUNDAY:
                return "Sunday";
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
            case SATURDAY:
                return "Saturday";
            default:
                return "NoDay";
        }
    }

    public static int getNextDay(int day) throws Exception {
        switch (day) {
            case SUNDAY:
                return MONDAY;
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
            default:
                throw new Exception("Invalid day inserted");
        }
    }

    // TODO - to delete
    public static int getDaysDifference(int dayToCompare, int comparisonDay) {
        // is true if the dayToCompare is after the comparisonDay
        if (dayToCompare < comparisonDay) {
            return 7 - comparisonDay + dayToCompare;
        } else {
            return dayToCompare - comparisonDay;
        }
    }

    // TODO - delete
    public static int compare(int day, int comparisonDay, int pivotDay) {
        // case in which day is before comparisonDay
        if (day < comparisonDay) {
            // day occurs after the pivotDay
            if (day >= pivotDay) return -1;
            else {
                // comparisonDay is after the pivotDay
                if (comparisonDay >= pivotDay) return 1;
                else return -1;
            }
        }
        // case in which day is after comparisonDay
        else if (day > comparisonDay) {
            if (comparisonDay >= pivotDay) return 1;
            else {
                if (day >= pivotDay) return -1;
                else return 1;
            }
        } else {
            // same day
            return 0;
        }
    }

    public static int[] reorderDays(int[] days) {
        List<Integer> list = new ArrayList<>();

        // Insert all valid days into the list
        for (int day : days) {
            if (day <= 7 && day >= 1) {
                Integer integerDay = new Integer(day);
                // Check uniqueness in the list
                if (!list.contains(integerDay)) {
                    list.add(integerDay);
                }
            }
        }

        // Reorder the list
        Collections.sort(list);

        // Create new array of reordered days
        int[] newDays = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            newDays[i] = list.get(i).intValue();
        }

        return newDays;
    }
}
