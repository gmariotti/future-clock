package com.mariotti.developer.futureclock.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.activities.AlarmActivity;
import com.mariotti.developer.futureclock.activities.AlarmFiredActivity;
import com.mariotti.developer.futureclock.model.Alarm;
import com.mariotti.developer.futureclock.model.OpenMapWeather;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import rx.android.schedulers.AndroidSchedulers;

public class FutureClockFragment extends Fragment {
    private static final String TAG = "FutureClockFragment";
    private static final int REQUEST_CODE = 57;

    private Button mOkButton;
    private Button mVoiceButton;
    private TextView mWeatherText;

    private FloatingActionButton mAlarmFab;
    private RecyclerView mAlarmRecyclerView;
    private AlarmAdapter mAdapter;

    public static FutureClockFragment newInstance() {
        return new FutureClockFragment();
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

        mOkButton = (Button) view.findViewById(R.id.ok_button);
        mVoiceButton = (Button) view.findViewById(R.id.voice_button);
        mVoiceButton.setEnabled(false);
        mWeatherText = (TextView) view.findViewById(R.id.current_weather_text);

        // Part about the alarm list
        mAlarmFab = (FloatingActionButton) view.findViewById(R.id.alarm_fab);
        mAlarmFab.setOnClickListener(v -> {
            Intent intent = AlarmActivity.newIntent(getActivity(), null);
            startActivityForResult(intent, REQUEST_CODE);
        });
        mAlarmRecyclerView = (RecyclerView) view.findViewById(R.id.alarms_recycler_view);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Part about the weather and voice part
        mOkButton.setOnClickListener(v -> Observable.create(new Observable.OnSubscribe<OpenMapWeather>() {

            @Override
            public void call(Subscriber<? super OpenMapWeather> subscriber) {
                try {
                    subscriber.onNext(OpenMapWeatherFetchr.parseOpenMapWeather());
                    subscriber.onCompleted();
                } catch (IOException e) {
                    Snackbar.make(view, "Weather Error", Snackbar.LENGTH_LONG)
                            .setAction("", view -> Log.i(TAG, "parseOpenMapWeather error"))
                            .show();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OpenMapWeather>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError");
                    }

                    @Override
                    public void onNext(OpenMapWeather openMapWeather) {
                        mWeatherText.setText(openMapWeather.toString());
                        mVoiceButton.setEnabled(true);
                        Log.i(TAG, "onNext");
                    }
                }));

        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            TextToSpeech mTextToSpeech;

            @Override
            public void onClick(View v) {
                mTextToSpeech = new TextToSpeech(getActivity(), status -> {
                    String goodMorning = "Good morning Sir, ";
                    mTextToSpeech.speak(goodMorning + mWeatherText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                });
            }
        });

        updateUI();
        updateNextAlarm();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // if result code is RESULT_OK then an alarm as been updated or added
        if (requestCode == REQUEST_CODE) {
            updateUI();
            updateNextAlarm();
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

    private void updateUI() {
        AlarmController controller = AlarmController.getAlarmController(getActivity());
        List<Alarm> alarms = controller.getAlarms();

        if (mAdapter == null) {
            mAdapter = new AlarmAdapter(alarms);
            mAlarmRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setAlarms(alarms);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateNextAlarm() {
        // check if the alarms list of active alarm is not empty
        if (!AlarmController.getAlarmController(getActivity()).getActiveAlarms().isEmpty()) {
            // get the first alarm valid for the current day
            Alarm alarm = AlarmController.getAlarmController(getActivity()).getNextAlarm();
            if (alarm != null) {
                AlarmFiredActivity.setActivityAlarm(getActivity(), alarm.getUUID());
            }
        }
    }

    private class AlarmHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTimeTextView;
        private TextView mDaysTextView;
        private Switch mActiveSwitch;

        private Alarm mAlarm;

        public AlarmHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_time);
            mDaysTextView = (TextView) itemView.findViewById(R.id.list_item_days);
            mActiveSwitch = (Switch) itemView.findViewById(R.id.list_item_switch);
        }

        public void bindAlarm(final Alarm alarm) {
            mAlarm = alarm;
            mTimeTextView.setText(mAlarm.getTime());
            mDaysTextView.setText(mAlarm.getDaysString());
            mActiveSwitch.setChecked(mAlarm.isActive());

            mActiveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mAlarm.setActive(isChecked);
                AlarmController.getAlarmController(getActivity()).updateAlarm(alarm);
                updateNextAlarm();
            });
        }

        @Override
        public void onClick(View v) {
            // Update the clicked alarm
            Intent intent = AlarmActivity.newIntent(getActivity(), mAlarm.getUUID());
            startActivityForResult(intent, REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            // TODO -> implement dialog confirmation
            if (AlarmController.getAlarmController(getActivity()).deleteAlarm(mAlarm.getUUID()) != 1) {
                Snackbar.make(v, "Error delete alarm", Snackbar.LENGTH_LONG)
                        .setAction("", view -> {
                            Log.i(TAG, "onLongClick event");
                        })
                        .show();
            } else {
                updateUI();
                updateNextAlarm();
            }
            return true;
        }
    }

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {
        private List<Alarm> mAlarms;

        public AlarmAdapter(List<Alarm> alarms) {
            mAlarms = alarms;
        }

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_alarm, parent, false);

            return new AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position) {
            Alarm alarm = mAlarms.get(position);
            holder.bindAlarm(alarm);
        }

        @Override
        public int getItemCount() {
            return mAlarms.size();
        }

        public void setAlarms(List<Alarm> alarms) {
            mAlarms = alarms;
        }
    }
}
