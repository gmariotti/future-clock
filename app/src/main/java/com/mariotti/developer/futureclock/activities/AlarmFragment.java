package com.mariotti.developer.futureclock.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.controller.AlarmController;
import com.mariotti.developer.futureclock.model.Alarm;

import java.util.UUID;

public class AlarmFragment extends Fragment {
    private static final String UUID_ARG = "UUID_ARG";

    private Alarm mAlarm;

    public static AlarmFragment newInstance(UUID uuid) {
        AlarmFragment alarmFragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putSerializable(UUID_ARG, uuid);
        alarmFragment.setArguments(args);

        return alarmFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        UUID alarmUuid = (UUID) getArguments().getSerializable(UUID_ARG);
        if (alarmUuid != null) {
            Alarm alarm = AlarmController.getAlarmController(getActivity()).getAlarm(alarmUuid);
            if (alarm != null) {
                mAlarm = alarm;
            }
        } else {
            mAlarm = new Alarm();
        }

        // TODO -> get layout element and update it with alarm values

        return view;
    }
}
