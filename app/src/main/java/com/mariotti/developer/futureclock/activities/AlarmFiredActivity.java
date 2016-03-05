package com.mariotti.developer.futureclock.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.mariotti.developer.futureclock.controllers.AlarmManagementController;
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController;
import com.mariotti.developer.futureclock.controllers.fragments.AlarmFiredFragment;
import com.mariotti.developer.futureclock.models.Alarm;

import java.util.Calendar;
import java.util.UUID;

public class AlarmFiredActivity extends SingleFragmentActivity {
    private static final String EXTRA_ALARM_FIRED_UUID = "com.mariotti.developer.futureclock.activities.AlarmFiredActivity";
    private static final String TAG = "AlarmFiredActivity";
    private static final int REQUEST_CODE = 493;

    public static Intent newIntent(Context context) {
        return new Intent(context, AlarmFiredActivity.class);
    }

    public static void setActivityAlarm(Context context, UUID uuid) {
        Intent intent = AlarmFiredActivity.newIntent(context);
        intent.putExtra(EXTRA_ALARM_FIRED_UUID, uuid);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Alarm alarm = DatabaseAlarmController.Companion.getInstance(context).getAlarm(uuid);
        Alarm alarm = null;
        if (alarm != null) {
            long alarmTime = AlarmManagementController.getNearestDayForAlarm(alarm, Calendar.getInstance())
                    .getTimeInMillis();

            Intent intentAlarmInfo = FutureClockActivity.Companion.newIntent(context);
            PendingIntent pendingIntentAlarmInfo = PendingIntent.getActivity(context, REQUEST_CODE, intentAlarmInfo, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager.AlarmClockInfo clockInfo = new AlarmManager.AlarmClockInfo(alarmTime, pendingIntentAlarmInfo);
            alarmManager.setAlarmClock(clockInfo, pendingIntent);

        } else {
            Log.d(TAG, "No alarm set with UUID = " + uuid.toString());
        }
    }

    public static void cancelAlarm(Context context) {
        Intent intent = AlarmFiredActivity.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "PendingIntent deleted");
    }

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_ALARM_FIRED_UUID);
        Log.d(TAG, "UUID = " + uuid.toString());

        return AlarmFiredFragment.newInstance(uuid);
    }
}
