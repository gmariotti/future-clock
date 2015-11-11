package com.mariotti.developer.futureclock.model;

import android.util.Log;

import java.util.EnumSet;
import java.util.UUID;

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
            throws IllegalArgumentException{
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
            for (WeekDay day : mDays) {
                result += day.getShortString();
            }
        }

        return result;
    }

    public void addDay(WeekDay day) {
        if (mDays.add(day)) {
            Log.d(TAG, "Day added " + day.getShortString());
        }
    }

    public void removeDay(WeekDay day) {
        if (mDays.remove(day)) {
            Log.d(TAG, "Day removed " + day.getShortString());
        }

    }

    public boolean hasMonday() {
        return mDays.contains(WeekDay.MONDAY);
    }

    public boolean hasTuesday() {
        return mDays.contains(WeekDay.TUESDAY);
    }
    public boolean hasWednesday() {
        return mDays.contains(WeekDay.WEDNESDAY);
    }
    public boolean hasThursday() {
        return mDays.contains(WeekDay.THURSDAY);
    }
    public boolean hasFriday() {
        return mDays.contains(WeekDay.FRIDAY);
    }
    public boolean hasSaturday() {
        return mDays.contains(WeekDay.SATURDAY);
    }
    public boolean hasSunday() {
        return mDays.contains(WeekDay.SUNDAY);
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
