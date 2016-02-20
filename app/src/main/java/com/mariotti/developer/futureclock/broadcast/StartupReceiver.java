package com.mariotti.developer.futureclock.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*Alarm alarm = UIAlarmController.getAlarmController(context)
                .getNextAlarm();
        if (alarm != null) {
            AlarmFiredActivity.setActivityAlarm(context, alarm.getUUID());
            Log.d(TAG, "Alarm set");
        }*/
    }
}
