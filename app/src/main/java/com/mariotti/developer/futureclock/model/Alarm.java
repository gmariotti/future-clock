package com.mariotti.developer.futureclock.model;

import android.util.Log;

import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.UUID;

import static com.mariotti.developer.futureclock.model.WeekDay.*;

public class Alarm {
    private static final String TAG = "Alarm";

    private UUID mUUID;
    private int mHour;
    private int mMinute;
    private int mDays[];
    private boolean mActive;

    /**
     * Default constructor, mUUID is generated randomly, time is set to 00:00,
     * while mDays is an empty EnumSet
     */
    public Alarm() {
        this.mUUID = UUID.randomUUID();
        this.mHour = 0;
        this.mMinute = 0;
        mActive = false;
    }

    /**
     * Set the alarm based on the arguments.
     *
     * @param UUID   alarm identifier
     * @param hour   hour of the alarm
     * @param minute minute of the alarm
     * @param days   list of days in which the alarm is enabled
     * @throws IllegalArgumentException if hour and/or minute is a wrong value,
     *                                  or days.length is greater than 7
     */
    public Alarm(UUID UUID, int hour, int minute, int[] days, boolean active)
            throws IllegalArgumentException {
        // Throw an exception if an illegal time is inserted
        if (hour > 23 || hour < 0 || minute > 59 || minute < 0) {
            throw new IllegalArgumentException();
        }

        if (days.length > 7) {
            throw new IllegalArgumentException();
        } else {
            for (int day : days) {
                if (day > 7 || day < 1) {
                    throw new IllegalArgumentException();
                }
            }
        }

        mUUID = UUID;
        mHour = hour;
        mMinute = minute;
        mDays = WeekDay.reorderDays(days);
        mActive = active;
    }

    public String getTime() {
        String hour = mHour < 10 ? "0" + mHour : Integer.toString(mHour);
        String minute = mMinute < 10 ? "0" + mMinute : Integer.toString(mMinute);
        String time = hour + ":" + minute;

        Log.d(TAG, "Time is " + time);

        return time;
    }

    @Override
    public String toString() {
        return "Alarm " + getTime() + " active on " + AlarmUtil.getShortDaysString(this);
    }

    /**
     * Get UUID
     *
     * @return UUID
     */
    public UUID getUUID() {
        return mUUID;
    }

    /**
     * Get the hour
     *
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
     *
     * @return minute
     */
    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        mMinute = minute;
    }

    /**
     * Get Array of days
     *
     * @return array of days
     */
    public int[] getDays() {
        return mDays;
    }

    public void setDays(int[] days) {
        mDays = WeekDay.reorderDays(days);
    }

    /**
     * Check if the alarm is active or not
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }
}
