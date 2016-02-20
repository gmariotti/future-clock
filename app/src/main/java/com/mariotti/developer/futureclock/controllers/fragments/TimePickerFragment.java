package com.mariotti.developer.futureclock.controllers.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

import com.mariotti.developer.futureclock.models.Alarm;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerFragment";
    private static final String ARG_HOUR = "ARG_HOUR";
    private static final String ARG_MINUTE = "ARG_MINUTE";

    public static final String EXTRA_TIME = "com.mariotti.developer.futureclock.controller.time";

    public TimePickerFragment() {
    }

    public static TimePickerFragment newInstance(Alarm alarm) {
        TimePickerFragment fragment = new TimePickerFragment();
        if (alarm != null) {
            Bundle args = new Bundle();
            args.putInt(ARG_HOUR, alarm.getHour());
            args.putInt(ARG_MINUTE, alarm.getMinute());
            fragment.setArguments(args);
        }

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            int hour = args.getInt(ARG_HOUR);
            int minute = args.getInt(ARG_MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hour, minute, true);

            return dialog;
        } else {
            return null;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (getTargetFragment() == null) {
            return;
        }

        String hourString = hourOfDay < 10 ? "0" + hourOfDay : Integer.toString(hourOfDay);
        String minuteString = minute < 10 ? "0" + minute : Integer.toString(minute);
        String time = hourString + ":" + minuteString;
        Log.d(TAG, "Time set is " + time);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
