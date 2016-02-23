package com.mariotti.developer.futureclock.controllers.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.activities.AlarmActivity;
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController;
import com.mariotti.developer.futureclock.models.Alarm;

import java.util.List;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListOfAlarmFragment extends AdapterFragment {
    private static final String TAG = "ListOfAlarmFragment";
    private static final String DIALOG_DELETE_ALARM = "DialogDeleteAlarm";

    private static final int REQUEST_CODE_ALARM_MANAGEMENT = 57;
    private static final int REQUEST_CODE_DELETE_ALARM = 902;

    private FloatingActionButton mAlarmFab;
    private RecyclerView mAlarmRecyclerView;
    private AlarmAdapter mAdapter;

    public static ListOfAlarmFragment newInstance() {
        return new ListOfAlarmFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_future_clock, container, false);

        mAlarmFab = (FloatingActionButton) view.findViewById(R.id.alarm_fab);
        mAlarmFab.setOnClickListener(v -> {
            Intent intent = AlarmActivity.newIntent(getActivity(), null);
            startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT);
        });
        mAlarmRecyclerView = (RecyclerView) view.findViewById(R.id.alarms_recycler_view);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateRecyclerViewList();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_ALARM_MANAGEMENT:
                updateRecyclerViewList();
                break;
            case REQUEST_CODE_DELETE_ALARM:
                manageAlarmDeletion(data);
                break;
            default:
        }
    }

    private void manageAlarmDeletion(Intent data) {
        boolean confirm = data.getBooleanExtra(AlarmDeleteFragment.EXTRA_DELETE_CONFIRM, false);
        if (confirm) {
            updateRecyclerViewList();
        } else {
            Snackbar.make(getView(), "Error deleting alarm", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.app_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Snackbar.make(getView(), "Settings", Snackbar.LENGTH_LONG)
                        .setAction("-> TODO", v -> Log.d(TAG, "Settings Selected"))
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateRecyclerViewList() {
        Observable.create(new Observable.OnSubscribe<List<Alarm>>() {
            @Override
            public void call(Subscriber<? super List<Alarm>> subscriber) {
                DatabaseAlarmController controller = DatabaseAlarmController.getDatabaseAlarmController(getActivity());
                List<Alarm> alarms = controller.getAlarms();
                subscriber.onNext(alarms);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Alarm>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onNext(List<Alarm> alarms) {
                        if (mAdapter == null) {
                            mAdapter = new AlarmAdapter(ListOfAlarmFragment.this, alarms);
                            mAlarmRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setAlarms(alarms);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });

        /*DatabaseAlarmController controller = DatabaseAlarmController.getDatabaseAlarmController(getActivity());
        List<Alarm> alarms = controller.getAlarms();

        if (mAdapter == null) {
            mAdapter = new AlarmAdapter(this, alarms);
            mAlarmRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setAlarms(alarms);
            mAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void modifyAlarm(UUID alarmUuid) {
        Intent intent = AlarmActivity.newIntent(getActivity(), alarmUuid);
        startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT);
    }

    @Override
    public void deleteAlarm(Alarm alarm) {
        AlarmDeleteFragment dialog = AlarmDeleteFragment.newInstance(alarm);
        dialog.setTargetFragment(this, REQUEST_CODE_DELETE_ALARM);
        FragmentManager manager = getFragmentManager();
        dialog.show(manager, DIALOG_DELETE_ALARM);
    }

}
