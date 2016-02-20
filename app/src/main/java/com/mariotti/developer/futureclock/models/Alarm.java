package com.mariotti.developer.futureclock.models;

import android.util.Log;

import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.Calendar;
import java.util.UUID;

public class Alarm {
    private static final String TAG = "Alarm";

    private UUID mUUID;
    private int mHour;
    private int mMinute;
    private int mDays[];
    private String timezone;
    private boolean mActive;

    public Alarm() {
        mUUID = UUID.randomUUID();
        mHour = 0;
        mMinute = 0;
    }

    public Alarm(UUID uuid, Calendar time, int days[], boolean active) throws Exception {
        if (time == null || uuid == null) {
            throw new Exception("Time and/or UUID are null");
        }

        mUUID = uuid;
        mHour = time.get(Calendar.HOUR_OF_DAY);
        mMinute = time.get(Calendar.MINUTE);
        setDays(days);
        timezone = time.getTimeZone().getDisplayName();
        mActive = active;
    }

    // TODO - to delete
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

    public String getTimeAsString() {
        String hour = mHour < 10 ? "0" + mHour : Integer.toString(mHour);
        String minute = mMinute < 10 ? "0" + mMinute : Integer.toString(mMinute);
        String time = hour + ":" + minute;

        Log.d(TAG, "Time is " + time);

        return time;
    }

    @Override
    public String toString() {
        return "Alarm " + getTimeAsString() + " active on " + AlarmUtil.getShortDaysString(this);
    }

    /********************
    Getter and Setter area
     ********************/

    public UUID getUUID() {
        return mUUID;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        mMinute = minute;
    }

    public int[] getDays() {
        return mDays;
    }

    public void setDays(int[] days) {
        mDays = WeekDay.reorderDays(days);
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

}
