package com.mariotti.developer.futureclock.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.model.Alarm;
import com.mariotti.developer.futureclock.model.WeekDay;

import java.util.EnumSet;
import java.util.UUID;

public class AlarmFragment extends Fragment {
    private static final String UUID_ARG = "UUID_ARG";

    private Alarm mAlarm;

    private TimePicker mTimePicker;
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
        setDaysSelected();

        // works only for API 23
        mTimePicker = (TimePicker) view.findViewById(R.id.alarm_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(mAlarm.getHour());
        mTimePicker.setMinute(mAlarm.getMinute());
        mTimePicker.setOnTimeChangedListener((timePicker, hourOfDay, minute) -> {
            mAlarm.setHour(hourOfDay);
            mAlarm.setMinute(minute);
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

    private TextView initializeDayTextView(View view, int idTextView, final WeekDay day) {
        final TextView textView = (TextView) view.findViewById(idTextView);
        textView.setOnClickListener(v -> {
            if (textView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                textView.setTextColor(getResources().getColor(R.color.red, null));
                mAlarm.addDay(day);
            } else {
                textView.setTextColor(getResources().getColor(R.color.grey, null));
                mAlarm.removeDay(day);
            }
        });
        return textView;
    }

    private void setDaysSelected() {
        EnumSet<WeekDay> enumSet = mAlarm.getDays();
        for (WeekDay day : enumSet) {
            switch (day) {
                case MONDAY:
                    mMondayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    break;
                case TUESDAY:
                    mTuesdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    break;
                case WEDNESDAY:
                    mWednesdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    break;
                case THURSDAY:
                    mThursdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    break;
                case FRIDAY:
                    mFridayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    break;
                case SATURDAY:
                    mSaturdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    break;
                case SUNDAY:
                    mSundayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    break;
                default:
                    break;
            }
        }
    }
}
