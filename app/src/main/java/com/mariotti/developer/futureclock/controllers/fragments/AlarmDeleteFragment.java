package com.mariotti.developer.futureclock.controllers.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController;
import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.UUID;

public class AlarmDeleteFragment extends DialogFragment {
    public static final String EXTRA_DELETE_CONFIRM = "com.mariotti.developer.futureclock.controller.confirm";
    private static final String TAG = "AlarmDeleteFragment";
    private static final String ARG_UUID = "ARG_UUID";
    private static final String ARG_TIME = "ARG_TIME";
    private static final String ARG_DAYS = "ARG_DAYS";

    public AlarmDeleteFragment() {
    }

    public static AlarmDeleteFragment newInstance(Alarm alarm) {
        AlarmDeleteFragment fragment = new AlarmDeleteFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UUID, alarm.getUuid());
        args.putString(ARG_TIME, AlarmUtil.getHourAndMinuteAsString(alarm.getHour(), alarm.getMinute()));
        args.putString(ARG_DAYS, AlarmUtil.getShortDaysString(alarm));
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            UUID uuid = (UUID) getArguments().getSerializable(ARG_UUID);
            String time = getArguments().getString(ARG_TIME);
            String days = getArguments().getString(ARG_DAYS);

            Log.d(TAG, "onCreateDialog");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.alarm_delete_dialog_title))
                    .setMessage(getResources().getString(R.string.alarm_delete_dialog_message)
                            + " " + time + " -> " + days)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        try {
                            DatabaseAlarmController.Companion.getInstance(getActivity())
                                    .deleteAlarm(uuid);
                            Log.d(TAG, "Alarm deleted");
                            sendResult(Activity.RESULT_OK, true);
                        } catch (Exception e) {
                            Log.d(TAG, "Error in deleting alarm");
                            sendResult(Activity.RESULT_CANCELED, false);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        Log.d(TAG, "cancel dialog");
                        sendResult(Activity.RESULT_CANCELED, false);
                    })
                    .create();
        } else {
            return null;
        }
    }

    private void sendResult(int resultCode, boolean confirm) {
        Log.d(TAG, "sendResult with confirm = " + confirm);

        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DELETE_CONFIRM, confirm);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
