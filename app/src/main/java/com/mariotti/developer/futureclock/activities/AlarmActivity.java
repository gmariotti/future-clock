package com.mariotti.developer.futureclock.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mariotti.developer.futureclock.controller.AlarmFragment;

import java.util.UUID;

public class AlarmActivity extends SingleFragmentActivity {
    private static final String EXTRA_ALARM_UUID = "com.mariotti.developer.futureclock.activities.alarm_uuid";

    /**
     * Used to create a new intent for starting the activity.
     * If alarmUuid is set, then is an update operation, otherwise is a
     * creation one
     * @param packageContext activity context for intent creation
     * @param alarmUuid present if is an update
     * @return the intent to start
     */
    public static Intent newIntent(Context packageContext, UUID alarmUuid) {
        Intent intent = new Intent(packageContext, AlarmActivity.class);

        if (alarmUuid != null) {
            intent.putExtra(EXTRA_ALARM_UUID, alarmUuid);
        }

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_ALARM_UUID);

        return AlarmFragment.newInstance(uuid);
    }
}
