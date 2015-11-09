package com.mariotti.developer.futureclock.controller;

import android.content.Context;

import com.mariotti.developer.futureclock.model.Alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class that acts as a controller between the Alarm object and the Alarm database.
 * Singleton pattern
 */
public class AlarmController {
    private static AlarmController sAlarmController;

    private Context mContext;
    private List<Alarm> mAlarms;

    private AlarmController(Context context) {
        // TODO -> why application context ??
        mContext = context.getApplicationContext();
    }

    public static AlarmController getAlarmController(Context context) {
        if (sAlarmController == null) {
            sAlarmController = new AlarmController(context);
        }
        return sAlarmController;
    }

    public List<Alarm> getAlarms() {
        // TODO -> get alarms from database
        if (mAlarms == null) {
            mAlarms = new ArrayList<>();
        }
        return mAlarms;
    }

    public void addAlarm(Alarm alarm) {
        // TODO -> add alarm to database and not to list
        if (mAlarms == null) {
            mAlarms = new ArrayList<>();
        }
        mAlarms.add(alarm);
    }

    public Alarm getAlarm(UUID uuid) {
        // TODO
        return null;
    }

    public void updateAlarm(Alarm alarm) {
        // TODO
    }
}
