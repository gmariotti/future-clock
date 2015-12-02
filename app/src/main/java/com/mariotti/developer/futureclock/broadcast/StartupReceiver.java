package com.mariotti.developer.futureclock.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mariotti.developer.futureclock.activities.AlarmFiredActivity;
import com.mariotti.developer.futureclock.controller.AlarmController;
import com.mariotti.developer.futureclock.model.Alarm;

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm alarm = AlarmController.getAlarmController(context)
                .getNextAlarm();
        if (alarm != null) {
            AlarmFiredActivity.setActivityAlarm(context, alarm.getUUID());
            Log.d(TAG, "Alarm set");
        }
    }
}
