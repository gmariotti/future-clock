package com.mariotti.developer.futureclock.util;

import android.util.Log;

import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.WeekDay;

public class AlarmUtil {
    private static final String TAG = "AlarmUtil";

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

    public static String getHourAndMinuteAsString(int hour, int minute) {
        String hourString = (hour < 10) ? "0" + hour : Integer.toString(hour);
        String minuteString = (minute < 10) ? "0" + minute : Integer.toString(minute);
        return hourString + ":" + minuteString;
    }

    public static int[] addDay(int days[], int day) {
        int[] newDays = new int[days.length + 1];
        for (int i = 0; i < days.length; i++) {
            newDays[i] = days[i];
        }
        newDays[days.length] = day;

        Log.d(TAG, "Added day " + WeekDay.getName(day));

        return newDays;
    }

    public static int[] removeDay(int days[], int day) {
        for (int i = 0; i < days.length; i++) {
            if (days[i] == day) {
                days[i] = -1;
                break;
            }
        }
        return WeekDay.reorderDays(days);
    }

    public static boolean hasDay(int days[], int day) {
        if (days.length != 0) {
            for (int dayToCompare : days) {
                if (dayToCompare == day) {
                    return true;
                }
            }
        }

        return false;
    }
}
