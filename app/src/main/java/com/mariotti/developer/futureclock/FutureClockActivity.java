package com.mariotti.developer.futureclock;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FutureClockActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return FutureClockFragment.newInstance();
    }
}
