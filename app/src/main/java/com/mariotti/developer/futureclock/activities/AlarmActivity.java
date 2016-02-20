package com.mariotti.developer.futureclock.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mariotti.developer.futureclock.controllers.fragments.AlarmFragment;

import java.util.UUID;

public class AlarmActivity extends SingleFragmentActivity {
    private static final String EXTRA_ALARM_UUID = "com.mariotti.developer.futureclock.activities.alarm_uuid";

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
