package com.mariotti.developer.futureclock.util;

import android.util.Log;

import com.mariotti.developer.futureclock.model.Alarm;
import com.mariotti.developer.futureclock.model.WeekDay;

import java.util.Calendar;

/**
 * This class removes all the logic inside Alarm
 */
public class AlarmUtil {
    private static final String TAG = "AlarmUtil";

    /**
     * Get the list of short name of the days of an alarm
     *
     * @param alarm variable to consider
     * @return list of short name
     */
    public static String getShortDaysString(Alarm alarm) {
        String result = "NoDay";
        if (alarm != null) {
            int[] days = alarm.getDays();

            if (days.length > 0) {
                result = "";
                for (int i = 0; i < days.length; i++) {
                    result += WeekDay.getShortName(days[i]);
                    if (i < (days.length - 1)) {
                        result += ", ";
                    }
                }
            }
        }

        return result;
    }

    /**
     * Insert a day into the alarm
     *
     * @param alarm variable in which insert the day
     * @param day   value to insert
     */
    public static void addDay(Alarm alarm, int day) {
        if (alarm != null) {
            int[] days = alarm.getDays();
            int[] newDays = new int[days.length + 1];
            for (int i = 0; i < days.length; i++) {
                newDays[i] = days[i];
            }
            newDays[days.length] = day;

            Log.d(TAG, "Added day " + WeekDay.getName(day));

            alarm.setDays(newDays);
        }
    }

    /**
     * Remove a day from an alarm
     *
     * @param alarm variable from which remove the day
     * @param day   value to remove
     */
    public static void removeDay(Alarm alarm, int day) {
        if (alarm != null) {
            int[] days = alarm.getDays();

            for (int i = 0; i < days.length; i++) {
                if (days[i] == day) {
                    days[i] = -1;
                    break;
                }
            }

            alarm.setDays(days);
        }
    }

    /**
     * Check if the day is present in the alarm or not
     *
     * @param alarm variable from which searching the day
     * @param day   value to search
     * @return true if present, false otherwise
     */
    public static boolean hasDay(Alarm alarm, int day) {
        if (alarm != null) {
            int[] days = alarm.getDays();

            if (days.length != 0) {
                for (int dayToCompare : days) {
                    if (dayToCompare == day) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get the nearest day of an alarm based on the day, hour and minute inserted
     *
     * @param alarm  variable to compare
     * @param day    represents the day from which getting the nearest
     * @param hour   represents the hour to consider
     * @param minute represents the minute to consider
     * @return the nearest day, -1 in case of an invalid value
     */
    public static int getNearestDay(Alarm alarm, int day, int hour, int minute) {
        if (alarm != null) {
            if (day > 7 || day < 1 || hour > 23 || hour < 0 || minute > 59 || minute < 0) {
                return -1;
            }

            int[] days = alarm.getDays();
            int hourAlarm = alarm.getHour();
            int minuteAlarm = alarm.getMinute();

            if (days.length != 0) {
                if (hasDay(alarm, day) &&
                        (hourAlarm > hour || (hourAlarm == hour && minuteAlarm > minute))) {
                    return day;
                } else {
                    int nextDay = WeekDay.getNextDay(day);
                    while (!hasDay(alarm, day)) {
                        nextDay = WeekDay.getNextDay(nextDay);
                    }
                    return nextDay;
                }
            } else if (hourAlarm > hour || (hourAlarm == hour && minuteAlarm > minute)) {
                return day;
            } else {
                return WeekDay.getNextDay(day);
            }
        } else {
            return -1;
        }
    }

    /**
     * Get the time of an alarm in respect to a Calendar variable in milliseconds
     *
     * @param alarm    variable from which getting the time
     * @param calendar variable to compare with the alarm
     * @return the time in milliseconds
     */
    public static long getTimeInMillisRespectTo(Alarm alarm, Calendar calendar) {
        if (alarm != null && calendar != null) {
            int alarmHour = alarm.getHour();
            int alarmMinute = alarm.getMinute();

            long time = calendar.getTimeInMillis();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            // manage minutes and hours problem
            if (hour > alarmHour || (hour == alarmHour && minute > alarmMinute)) {
                // add to time the milliseconds to become a new day
                time += (60 - minute) * 60 * 1000;
                time += (23 - hour) * 3600 * 1000;
                day = WeekDay.getNextDay(day);
                Log.d(TAG, "NextDay is " + WeekDay.getShortName(day));
            } else {
                if (minute > alarmMinute) {
                    hour++;
                    minute = 0;
                }
            }
            time += (alarmHour - hour) * 3600 * 1000;
            time += (alarmMinute - minute) * 60 * 1000;

            // add the time difference based on the day to consider
            int nearestDay = getNearestDay(alarm, day, hour, minute);
            Log.d(TAG, "Nearest day is " + WeekDay.getShortName(nearestDay));
            int dayDifference = WeekDay.getDaysDifference(day, nearestDay);
            Log.d(TAG, "Day difference is " + dayDifference);

            time += dayDifference * 24 * 3600 * 1000;

            calendar.setTimeInMillis(time);
            Log.d(TAG, "Alarm day " + calendar.get(Calendar.DAY_OF_WEEK)
                    + " at " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE));

            return time;
        } else {
            return -1;
        }
    }
}
