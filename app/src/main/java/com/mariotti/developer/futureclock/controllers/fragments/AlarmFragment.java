package com.mariotti.developer.futureclock.controllers.fragments;

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
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController;
import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.WeekDay;
import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.Calendar;
import java.util.UUID;

public class AlarmFragment extends Fragment {
    private static final String TAG = "AlarmFragment";
    private static final String DIALOG_TIME = "TimePickerFragment";
    private static final String UUID_ARG = "UUID_ARG";

    private static final int REQUEST_CODE_TIME = 565;

    private UUID mUUID;
    private int mHour;
    private int mMinute;
    private int mDays[];
    private boolean mActive;

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
        if (uuid != null) {
            Bundle args = new Bundle();
            args.putSerializable(UUID_ARG, uuid);
            alarmFragment.setArguments(args);
        }

        return alarmFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        Alarm alarm = getAlarm();
        mUUID = alarm.getUuid();
        mHour = alarm.getHour();
        mMinute = alarm.getMinute();
        mDays = alarm.getDays();
        mActive = alarm.getActive();

        initializeDaysListTextView(view);

        mTimeTextView = (TextView) view.findViewById(R.id.alarm_time_picker);
        mTimeTextView.setText(AlarmUtil.getHourAndMinuteAsString(alarm.getHour(), alarm.getMinute()));
        mTimeTextView.setOnClickListener(v -> {
            TimePickerFragment dialog = TimePickerFragment.newInstance(mHour, mMinute);
            dialog.setTargetFragment(AlarmFragment.this, REQUEST_CODE_TIME);
            dialog.show(getFragmentManager(), DIALOG_TIME);
        });

        mSwitch = (Switch) view.findViewById(R.id.alarm_switch);
        mSwitch.setChecked(mActive);
        mSwitch.setOnClickListener(v -> mActive = !mActive);

        mConfirmButton = (Button) view.findViewById(R.id.alarm_confirm_button);
        mConfirmButton.setOnClickListener(v -> {
            // TODO - use RxJava
            try {
                DatabaseAlarmController controller =
                        DatabaseAlarmController.getDatabaseAlarmController(getActivity());
                Calendar day = Calendar.getInstance();
                day.set(Calendar.HOUR_OF_DAY, mHour);
                day.set(Calendar.MINUTE, mMinute);
                Alarm alarmToInsert = new Alarm(mUUID, day, mDays, mActive);
                if (!checkIfUpdateOperation()) {
                    controller.addAlarm(alarmToInsert);
                } else {
                    controller.updateAlarm(alarmToInsert);
                }
                AlarmFragment.this.getActivity().setResult(Activity.RESULT_OK);
                AlarmFragment.this.getActivity().finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    private Alarm getAlarm() {
        Alarm alarmToUpdate = null;

        if (checkIfUpdateOperation()) {
            UUID alarmUuid = (UUID) getArguments().getSerializable(UUID_ARG);
            // TODO - use RxJava
            alarmToUpdate = DatabaseAlarmController.getDatabaseAlarmController(getActivity())
                    .getAlarm(alarmUuid);
        }

        return alarmToUpdate != null ? alarmToUpdate
                : new Alarm(UUID.randomUUID(), Calendar.getInstance(), new int[]{}, false);
    }

    private boolean checkIfUpdateOperation() {
        return getArguments() != null && getArguments().getSerializable(UUID_ARG) != null;
    }

    private void initializeDaysListTextView(View view) {
        mMondayTextView = initializeDayTextView(view, R.id.alarm_textview_mon, WeekDay.MONDAY);
        mTuesdayTextView = initializeDayTextView(view, R.id.alarm_textview_tue, WeekDay.TUESDAY);
        mWednesdayTextView = initializeDayTextView(view, R.id.alarm_textview_wed, WeekDay.WEDNESDAY);
        mThursdayTextView = initializeDayTextView(view, R.id.alarm_textview_thu, WeekDay.THURSDAY);
        mFridayTextView = initializeDayTextView(view, R.id.alarm_textview_fri, WeekDay.FRIDAY);
        mSaturdayTextView = initializeDayTextView(view, R.id.alarm_textview_sat, WeekDay.SATURDAY);
        mSundayTextView = initializeDayTextView(view, R.id.alarm_textview_sun, WeekDay.SUNDAY);
    }

    private TextView initializeDayTextView(View view, int idTextView, final int day) {
        final TextView textView = (TextView) view.findViewById(idTextView);
        textView.setOnClickListener(v -> {
            // Compatibility with version before Android M - API 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (textView.getCurrentTextColor() == getResources().getColor(R.color.grey, null)) {
                    textView.setTextColor(getResources().getColor(R.color.colorAccent, null));
                    mDays = AlarmUtil.addDay(mDays, day);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.grey, null));
                    mDays = AlarmUtil.removeDay(mDays, day);
                }
            } else {
                if (textView.getCurrentTextColor() == getResources().getColor(R.color.grey)) {
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    mDays = AlarmUtil.addDay(mDays, day);
                } else {
                    textView.setTextColor(getResources().getColor(R.color.grey));
                    mDays = AlarmUtil.removeDay(mDays, day);
                }
            }
        });

        if (AlarmUtil.hasDay(mDays, day)) {
            // Compatibility with version before Android M - API 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent, null));
            } else {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }

        return textView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_TIME) {
            mHour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, mHour);
            mMinute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, mMinute);

            mTimeTextView.setText(AlarmUtil.getHourAndMinuteAsString(mHour, mMinute));
        }
    }
}
