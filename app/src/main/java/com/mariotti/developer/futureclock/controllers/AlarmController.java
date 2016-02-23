package com.mariotti.developer.futureclock.controllers;

import com.mariotti.developer.futureclock.models.Alarm;

import java.util.Calendar;
import java.util.List;

public class AlarmController {
    private static AlarmController sAlarmController;

    private AlarmController() {
    }

    public static AlarmController getAlarmController() {
        if (sAlarmController == null) {
            sAlarmController = new AlarmController();
        }
        return sAlarmController;
    }

    public Alarm getNextAlarm(List<Alarm> alarms) {
        if (alarms.size() == 0) {
            return null;
        }

        Calendar today = Calendar.getInstance();
        Calendar nearestDay = null;
        int index = 0;
        for (int i = 0; i < alarms.size(); i++) {
            Calendar alarmNearestDay = getNearestDayForAlarm(alarms.get(i), (Calendar) today.clone());
            if (nearestDay == null || alarmNearestDay.before(nearestDay)) {
                nearestDay = alarmNearestDay;
                index = i;
            }
        }

        return alarms.get(index);
    }

    public Calendar getNearestDayForAlarm(Alarm alarm, Calendar day) {
        Calendar initializedDay = initializeHourMinuteAndTimezone(alarm, day);

        int days[] = alarm.getDays();
        if (days.length == 0) {
            return initializedDay;
        }

        Calendar nearestDay = setCorrectDay((Calendar) initializedDay.clone(), days[0]);
        for (int i = 1; i < days.length; i++) {
            Calendar compareDay = setCorrectDay((Calendar) initializedDay.clone(), days[i]);
            if (nearestDay.after(compareDay)) {
                nearestDay = compareDay;
            }
        }

        return nearestDay;
    }

    private Calendar initializeHourMinuteAndTimezone(Alarm alarm, Calendar day) {
        Calendar dayToReturn = (Calendar) day.clone();
        dayToReturn.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        dayToReturn.set(Calendar.MINUTE, alarm.getMinute());
        if (!alarm.getTimezone().equals(dayToReturn.getTimeZone().getDisplayName())) {
            // TODO - modify timezone accordingly
        }
        correctWeekDay(day, dayToReturn);

        return dayToReturn;
    }

    private Calendar setCorrectDay(Calendar day, int weekDay) {
        Calendar dayToReturn = (Calendar) day.clone();
        dayToReturn.set(Calendar.DAY_OF_WEEK, weekDay);
        correctWeekDay(day, dayToReturn);

        return dayToReturn;
    }

    private void correctWeekDay(Calendar day, Calendar dayToReturn) {
        if (dayToReturn.before(day)) {
            dayToReturn.set(Calendar.WEEK_OF_YEAR, dayToReturn.get(Calendar.WEEK_OF_YEAR) + 1);
        }
    }
}
