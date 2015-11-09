package com.mariotti.developer.futureclock.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.activities.AlarmActivity;
import com.mariotti.developer.futureclock.model.Alarm;
import com.mariotti.developer.futureclock.model.OpenMapWeather;

import java.io.IOException;
import java.util.List;

public class FutureClockFragment extends Fragment {
    private static final String TAG = "FutureClockFragment";
    private static final int REQUEST_CODE = 57;

    private Button mOkButton;
    private Button mVoiceButton;
    private TextView mWeatherText;

    private Button mAlarmButton;
    private RecyclerView mAlarmRecyclerView;
    private AlarmAdapter mAdapter;

    public static FutureClockFragment newInstance() {
        return new FutureClockFragment();
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
        mAlarmButton = (Button) view.findViewById(R.id.alarm_button);
        mAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AlarmActivity.newIntent(getActivity(), null);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mAlarmRecyclerView = (RecyclerView) view.findViewById(R.id.alarms_recycler_view);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Part about the weather and voice part
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WeatherAsyncTask().execute();
            }
        });

        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            TextToSpeech mTextToSpeech;

            @Override
            public void onClick(View v) {
                mTextToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        String goodMorning = "Good morning Sir, ";
                        mTextToSpeech.speak(goodMorning + mWeatherText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
            }
        });

        updateUI();

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

    private class AlarmHolder extends RecyclerView.ViewHolder {
        private TextView mTimeTextView;
        private TextView mDaysTextView;
        private CheckBox mActiveCheckBox;

        private Alarm mAlarm;

        public AlarmHolder(View itemView) {
            super(itemView);

            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_time);
            mDaysTextView = (TextView) itemView.findViewById(R.id.list_item_days);
            mActiveCheckBox = (CheckBox) itemView.findViewById(R.id.active_checkbox);
        }

        public void bindAlarm(Alarm alarm) {
            mAlarm = alarm;
            mTimeTextView.setText(mAlarm.getTime());
            mDaysTextView.setText(mAlarm.getDaysString());
            mActiveCheckBox.setChecked(mAlarm.isActive());

            // TODO -> change the value of active in case the checkbox is clicked or not
            mActiveCheckBox.setEnabled(false);
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

    private class WeatherAsyncTask extends AsyncTask<Void, Void, Void> {
        private OpenMapWeather mWeather;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mWeather = OpenMapWeatherFetchr.parseOpenMapWeather();
            } catch (IOException e) {
                Log.d(TAG, "Error in getting the weather");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mWeather != null) {
                mWeatherText.setText(mWeather.toString());
                mVoiceButton.setEnabled(true);
            } else {
                Log.d(TAG, "mWeather is null");
            }
        }
    }
}
