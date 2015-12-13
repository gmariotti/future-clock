package com.mariotti.developer.futureclock.model;

import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represent the days of the week, considering Sunday as the first day of the week.
 * Day's value is equal to the value of the Calendar class
 */
public class WeekDay {
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;

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

    /**
     * Return the next day based on the value passed as an argument
     *
     * @param day
     * @return the next day, -1 if a invalid day is inserted
     */
    public static int getNextDay(int day) {
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
                return -1;
        }
    }

    /**
     * Get the difference in days between two day
     * Example: Sunday to Tuesday -> 3 - 1 = 2 days difference
     * Example: Saturday to Tuesday -> 7 - 7 + 3 = 3 days difference
     *
     * @param dayToCompare
     * @param comparisonDay
     * @return
     */
    public static int getDaysDifference(int dayToCompare, int comparisonDay) {
        // is true if the dayToCompare is after the comparisonDay
        if (dayToCompare > comparisonDay) {
            return 7 - comparisonDay + dayToCompare;
        } else {
            return dayToCompare - comparisonDay;
        }
    }

    /**
     * Compares two days in respect to a pivot day.
     *
     * @param day           first day of comparison
     * @param comparisonDay second day of comparison
     * @param pivotDay      pivot to which confront
     * @return -1 if day occurs before comparisonDay, 1 if it occurs after and
     * 0 if they are the same day
     */
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
