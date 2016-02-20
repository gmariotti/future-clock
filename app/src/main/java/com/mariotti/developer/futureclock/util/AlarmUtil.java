package com.mariotti.developer.futureclock.util;

import android.util.Log;

import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.WeekDay;

import java.util.Calendar;

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
}
