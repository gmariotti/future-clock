package com.mariotti.developer.futureclock.controllers.fragments;

import android.support.v4.app.Fragment;

import com.mariotti.developer.futureclock.models.Alarm;

import java.util.UUID;

public abstract class AdapterFragment extends Fragment {
    public abstract void modifyAlarm(UUID alarmUuid);

    public abstract void deleteAlarm(Alarm alarm);
}
