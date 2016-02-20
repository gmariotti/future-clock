package com.mariotti.developer.futureclock.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mariotti.developer.futureclock.controllers.fragments.ListOfAlarmFragment;

public class FutureClockActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, FutureClockActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return ListOfAlarmFragment.newInstance();
    }
}
