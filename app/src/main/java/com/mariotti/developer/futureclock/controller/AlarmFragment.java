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

        mMondayTextView = (TextView) view.findViewById(R.id.alarm_textview_mon);
        mMondayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMondayTextView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    mMondayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    mAlarm.addDay(WeekDay.MONDAY);
                } else {
                    mMondayTextView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(WeekDay.MONDAY);
                }
            }
        });
        mTuesdayTextView = (TextView) view.findViewById(R.id.alarm_textview_tue);
        mTuesdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTuesdayTextView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    mTuesdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    mAlarm.addDay(WeekDay.TUESDAY);
                } else {
                    mTuesdayTextView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(WeekDay.TUESDAY);
                }
            }
        });
        mWednesdayTextView = (TextView) view.findViewById(R.id.alarm_textview_wed);
        mWednesdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWednesdayTextView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    mWednesdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    mAlarm.addDay(WeekDay.WEDNESDAY);
                } else {
                    mWednesdayTextView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(WeekDay.WEDNESDAY);
                }
            }
        });
        mThursdayTextView = (TextView) view.findViewById(R.id.alarm_textview_thu);
        mThursdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mThursdayTextView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    mThursdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    mAlarm.addDay(WeekDay.THURSDAY);
                } else {
                    mThursdayTextView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(WeekDay.THURSDAY);
                }
            }
        });
        mFridayTextView = (TextView) view.findViewById(R.id.alarm_textview_fri);
        mFridayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFridayTextView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    mFridayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    mAlarm.addDay(WeekDay.FRIDAY);
                } else {
                    mFridayTextView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(WeekDay.FRIDAY);
                }
            }
        });
        mSaturdayTextView = (TextView) view.findViewById(R.id.alarm_textview_sat);
        mSaturdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSaturdayTextView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    mSaturdayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    mAlarm.addDay(WeekDay.SATURDAY);
                } else {
                    mSaturdayTextView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(WeekDay.SATURDAY);
                }
            }
        });
        mSundayTextView = (TextView) view.findViewById(R.id.alarm_textview_sun);
        mSundayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSundayTextView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    mSundayTextView.setTextColor(getResources().getColor(R.color.red, null));
                    mAlarm.addDay(WeekDay.SUNDAY);
                } else {
                    mSundayTextView.setTextColor(getResources().getColor(R.color.grey, null));
                    mAlarm.removeDay(WeekDay.SUNDAY);
                }
            }
        });
        setDaysSelected();

        // works only for API 23
        mTimePicker = (TimePicker) view.findViewById(R.id.alarm_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(mAlarm.getHour());
        mTimePicker.setMinute(mAlarm.getMinute());
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mAlarm.setHour(hourOfDay);
                mAlarm.setMinute(minute);
            }
        });

        mSwitch = (Switch) view.findViewById(R.id.alarm_switch);
        mSwitch.setChecked(mAlarm.isActive());
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlarm.setActive(mSwitch.isChecked());
            }
        });

        mConfirmButton = (Button) view.findViewById(R.id.alarm_confirm_button);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmUuid == null) {
                    AlarmController.getAlarmController(getActivity()).addAlarm(mAlarm);
                } else {
                    AlarmController.getAlarmController(getActivity()).updateAlarm(mAlarm);
                }
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        return view;
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
