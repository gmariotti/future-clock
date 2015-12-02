package com.mariotti.developer.futureclock.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.model.Alarm;

import java.util.UUID;

public class AlarmDeleteFragment extends DialogFragment {
    private static final String TAG = "AlarmDeleteFragment";
    private static final String ARG_UUID = "ARG_UUID";
    private static final String ARG_TIME = "ARG_TIME";
    private static final String ARG_DAYS = "ARG_DAYS";

    public static final String EXTRA_DELETE_CONFIRM = "com.mariotti.developer.futureclock.controller.confirm";

    public AlarmDeleteFragment() {
    }

    public static AlarmDeleteFragment newInstance(Alarm alarm) {
        AlarmDeleteFragment fragment = new AlarmDeleteFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_UUID, alarm.getUUID());
        args.putString(ARG_TIME, alarm.getTime());
        args.putString(ARG_DAYS, alarm.getDaysString());
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        UUID uuid = (UUID) getArguments().getSerializable(ARG_UUID);
        String time = getArguments().getString(ARG_TIME);
        String days = getArguments().getString(ARG_DAYS);

        Log.d(TAG, "onCreateDialog");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.alarm_delete_dialog_title))
                .setMessage(getResources().getString(R.string.alarm_delete_dialog_message)
                        + " " + time + " -> " + days)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (AlarmController.getAlarmController(getActivity()).deleteAlarm(uuid) == 1) {
                        Log.d(TAG, "Alarm deleted");
                        sendResult(Activity.RESULT_OK, true);
                    } else {
                        Log.d(TAG, "Error in deleting alarm");
                        sendResult(Activity.RESULT_CANCELED, false);
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    Log.d(TAG, "cancel dialog");
                    sendResult(Activity.RESULT_CANCELED, false);
                })
                .create();
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
