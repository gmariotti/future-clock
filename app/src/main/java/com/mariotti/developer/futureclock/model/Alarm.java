package com.mariotti.developer.futureclock.model;

import android.util.Log;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.UUID;

import static com.mariotti.developer.futureclock.model.WeekDay.*;

public class Alarm {
    private static final String TAG = "Alarm";

    private UUID mUUID;
    private int mHour;
    private int mMinute;
    private EnumSet<WeekDay> mDays;
    private boolean mActive;

    /**
     * Default constructor, mUUID is generated randomly, time is set to 00:00,
     * while mDays is an empty EnumSet
     */
    public Alarm() {
        this.mUUID = UUID.randomUUID();
        this.mHour = 0;
        this.mMinute = 0;
        mDays = EnumSet.noneOf(WeekDay.class);
    }

     /**
     * Set the alarm based on the arguments.
     * @param UUID alarm identifier
     * @param hour hour of the alarm
     * @param minute minute of the alarm
     * @param days list of days in which the alarm is enabled
     * @throws IllegalArgumentException if hour and/or minute is a wrong value
     */
    public Alarm(UUID UUID, int hour, int minute, EnumSet<WeekDay> days, boolean active)
            throws IllegalArgumentException {
        // Throw an exception if an illegal time is inserted
        if (hour > 23 || hour < 0 || minute > 59 || minute < 0) {
            throw new IllegalArgumentException();
        }

        mUUID = UUID;
        mHour = hour;
        mMinute = minute;
        mDays = days;
        mActive = active;
    }

    public String getTime() {
        String hour = mHour < 10 ? "0" + mHour : Integer.toString(mHour);
        String minute = mMinute < 10 ? "0" + mMinute : Integer.toString(mMinute);

        return hour + ":" + minute;
    }

    public String getDaysString() {
        String result = "NoDay";

        if (!mDays.isEmpty()) {
            result = "";
            int counter = 0;
            for (WeekDay day : mDays) {
                if (counter != 0) {
                    result += ", ";
                } else {
                    counter = 1;
                }
                result += day.getShortName();
            }
        }

        return result;
    }

    public void addDay(WeekDay day) {
        if (mDays.add(day)) {
            Log.d(TAG, "Day added " + day.getShortName());
        }
    }

    public WeekDay getNearestDay(WeekDay day, int hour, int minute) {
        if (!mDays.isEmpty()) {
            if (mDays.contains(day) && (mHour > hour || (mHour == hour && mMinute > minute))) {
                return day;
            } else {
                WeekDay nextDay = WeekDay.next(day);
                while (!mDays.contains(nextDay)) {
                    nextDay = WeekDay.next(nextDay);
                }
                return nextDay;
            }
        } else if(mHour > hour || (mHour == hour && mMinute > minute)) {
            return day;
        } else {
            return WeekDay.next(day);
        }
    }


    public long getTimeInMillisRespectTo(Calendar calendar) {
        long time = calendar.getTimeInMillis();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        WeekDay day = WeekDay.getFromInt(calendar.get(Calendar.DAY_OF_WEEK));

        // manage minutes and hours problem
        if (hour > mHour || (hour == mHour && minute > mMinute)) {
            // add to time the milliseconds to become a new day
            time += (60 - minute) * 60 * 1000;
            time += (23 - hour) * 3600 * 1000;
            day = next(day);
        } else {
            if (minute > mMinute) {
                hour++;
                minute = 0;
            }
            time += (mHour - hour) * 3600 * 1000;
            time += (mMinute - minute) * 60 * 1000;
        }

        // add the time difference based on the day to consider
        WeekDay nearestDay = getNearestDay(day, hour, minute);
        int dayDifference = nearestDay.getDayDifference(day);

        time += dayDifference * 24 * 3600 * 1000;

        return time;
    }

    public void removeDay(WeekDay day) {
        if (mDays.remove(day)) {
            Log.d(TAG, "Day removed " + day.getShortName());
        }

    }

    public boolean hasDay(WeekDay day) {
        return mDays.contains(day);
    }

    /**
     * Get UUID
     * @return UUID
     */
    public UUID getUUID() {
        return mUUID;
    }

    /**
     * Get the hour
     * @return hour
     */
    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    /**
     * Get the minute
     * @return minute
     */
    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        mMinute = minute;
    }

    /**
     * Get EnumSet of days
     * @return list of days
     */
    public EnumSet<WeekDay> getDays() {
        return mDays;
    }

    /**
     * Check if the alarm is active or not
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }
}
