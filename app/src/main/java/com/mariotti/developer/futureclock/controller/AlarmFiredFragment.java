package com.mariotti.developer.futureclock.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.model.Alarm;

import java.util.UUID;

public class AlarmFiredFragment extends Fragment {
    private static final String ARG_UUID = "ARG_UUID";

    private TextView mAlarmFiredTextView;

    public static AlarmFiredFragment newInstance(UUID uuid) {
        AlarmFiredFragment fragment = new AlarmFiredFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UUID, uuid);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_fired, container, false);

        mAlarmFiredTextView = (TextView) view.findViewById(R.id.alarm_fired_text_view);

        UUID uuid = (UUID) getArguments().getSerializable(ARG_UUID);
        Alarm alarm = AlarmController.getAlarmController(getActivity()).getAlarm(uuid);

        mAlarmFiredTextView.setText("Fired alarm at time " + alarm.getTime());

        return view;
    }
}
