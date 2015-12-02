package com.mariotti.developer.futureclock.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.model.Alarm;
import com.mariotti.developer.futureclock.model.WeekDay;

import java.util.UUID;

public class AlarmFragment extends Fragment {
    private static final String TAG = "AlarmFragment";
    private static final String DIALOG_TIME = "TimePickerFragment";
    private static final String UUID_ARG = "UUID_ARG";

    private static final int REQUEST_CODE_TIME = 565;

    private Alarm mAlarm;

    private TextView mTimeTextView;
    private TextView mMondayTextView;
    private TextView mTuesdayTextView;
    private TextView mWednesdayTextView;
    private TextView mThursdayTextView;
    private TextView mFridayTextView;
    private TextView mSaturdayTextView;
    private TextView mSundayTextView;
    private Switch mSwitch;
    private Button mConfirmButton;

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

        final UUID alarmUuid = (UUID) getArguments().getSerializable(UUID_ARG);
        if (alarmUuid != null) {
            Alarm alarm = AlarmController.getAlarmController(getActivity()).getAlarm(alarmUuid);
            if (alarm != null) {
                mAlarm = alarm;
            }
        } else {
            mAlarm = new Alarm();
        }

        mMondayTextView = initializeDayTextView(view, R.id.alarm_textview_mon, WeekDay.MONDAY);
        mTuesdayTextView = initializeDayTextView(view, R.id.alarm_textview_tue, WeekDay.TUESDAY);
        mWednesdayTextView = initializeDayTextView(view, R.id.alarm_textview_wed, WeekDay.WEDNESDAY);
        mThursdayTextView = initializeDayTextView(view, R.id.alarm_textview_thu, WeekDay.THURSDAY);
        mFridayTextView = initializeDayTextView(view, R.id.alarm_textview_fri, WeekDay.FRIDAY);
        mSaturdayTextView = initializeDayTextView(view, R.id.alarm_textview_sat, WeekDay.SATURDAY);
        mSundayTextView = initializeDayTextView(view, R.id.alarm_textview_sun, WeekDay.SUNDAY);

        mTimeTextView = (TextView) view.findViewById(R.id.alarm_time_picker);
        mTimeTextView.setText(mAlarm.getTime());
        mTimeTextView.setOnClickListener(v -> {
            TimePickerFragment dialog = TimePickerFragment.newInstance(mAlarm);
            dialog.setTargetFragment(AlarmFragment.this, REQUEST_CODE_TIME);
            dialog.show(getFragmentManager(), DIALOG_TIME);
        });

        mSwitch = (Switch) view.findViewById(R.id.alarm_switch);
        mSwitch.setChecked(mAlarm.isActive());
        mSwitch.setOnClickListener(v -> mAlarm.setActive(mSwitch.isChecked()));

        mConfirmButton = (Button) view.findViewById(R.id.alarm_confirm_button);
        mConfirmButton.setOnClickListener(v -> {
            if (alarmUuid == null) {
                AlarmController.getAlarmController(getActivity()).addAlarm(mAlarm);
            } else {
                AlarmController.getAlarmController(getActivity()).updateAlarm(mAlarm);
            }
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_TIME) {
            String newTime = data.getStringExtra(TimePickerFragment.EXTRA_TIME);
            mTimeTextView.setText(newTime);
            // TODO -> implement ":" as a constant in TimePickerFragment
            String[] time = newTime.split(":");
            mAlarm.setHour(Integer.parseInt(time[0]));
            mAlarm.setMinute(Integer.parseInt(time[1]));
        }
    }

    private TextView initializeDayTextView(View view, int idTextView, final WeekDay day) {
        final TextView textView = (TextView) view.findViewById(idTextView);
        textView.setOnClickListener(v -> {
            // Compatibility with version before Android M - API 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (textView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    textView.setTextColor(getResources().getColor(R.color.colorAccent, null));
                    mAlarm.addDay(day);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(day);
                }
            } else {
                if (textView.getCurrentTextColor() == getResources().getColor(R.color.grey)) {
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    mAlarm.addDay(day);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.grey));
                    mAlarm.removeDay(day);
                }
            }
        });

        if (mAlarm.hasDay(day)) {
            // Compatibility with version before Android M - API 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent, null));
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }

        return textView;
    }
}
