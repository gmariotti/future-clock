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

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    public static final String EXTRA_HOUR = "com.mariotti.developer.futureclock.controller.hour";
    public static final String EXTRA_MINUTE = "com.mariotti.developer.futureclock.controller.minute";

    private static final String TAG = "TimePickerFragment";
    private static final String ARG_HOUR = "ARG_HOUR";
    private static final String ARG_MINUTE = "ARG_MINUTE";

    public TimePickerFragment() {
    }

    public static TimePickerFragment newInstance(int hour, int minute) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_HOUR, hour);
        args.putInt(ARG_MINUTE, minute);
        fragment.setArguments(args);

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

        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hourOfDay);
        intent.putExtra(EXTRA_MINUTE, minute);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
