package com.mariotti.developer.futureclock.activities;

import android.support.v4.app.Fragment;

import com.mariotti.developer.futureclock.controller.FutureClockFragment;

public class FutureClockActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return FutureClockFragment.newInstance();
    }
}
