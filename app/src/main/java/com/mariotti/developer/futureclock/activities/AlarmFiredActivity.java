package com.mariotti.developer.futureclock.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mariotti.developer.futureclock.controller.AlarmController;
import com.mariotti.developer.futureclock.controller.AlarmFiredFragment;
import com.mariotti.developer.futureclock.model.Alarm;

import java.util.TimeZone;
import java.util.UUID;

public class AlarmFiredActivity extends SingleFragmentActivity {
    private static final String EXTRA_ALARM_FIRED_UUID = "com.mariotti.developer.futureclock.activities.AlarmFiredActivity";
    private static final String TAG = "AlarmFiredActivity";
    private static final int REQUEST_CODE = 493;

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_ALARM_FIRED_UUID);

        return AlarmFiredFragment.newInstance(uuid);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AlarmFiredActivity.class);
    }

    public static void setActivityAlarm(Context context, UUID uuid) {
        Intent intent = AlarmFiredActivity.newIntent(context);
        intent.putExtra(EXTRA_ALARM_FIRED_UUID, uuid);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Alarm alarm = AlarmController.getAlarmController(context).getAlarm(uuid);
        int hour = alarm.getHour();
        int minute = alarm.getMinute();
        // in milliseconds
        long alarmTime = (hour * 60 * 60 + minute * 60) * 1000;

        // considered the System Time Zone
        TimeZone timeZone = TimeZone.getDefault();
        Log.i(TAG, "Current time zone " + timeZone.getDisplayName());
        alarmTime = alarmTime + timeZone.getRawOffset();
        if (timeZone.useDaylightTime()) {
            alarmTime += timeZone.getDSTSavings();
        }

        Log.i(TAG, "Actual time in hour " + alarmTime / 1000 / 60 / 60);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
    }
}
