package com.mariotti.developer.futureclock.controllers.fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController;
import com.mariotti.developer.futureclock.controllers.OpenMapWeatherFetchr;
import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.OpenMapWeather;
import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.io.IOException;
import java.util.UUID;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlarmFiredFragment extends Fragment {
    private static final String TAG = "AlarmFiredFragment";
    private static final String ARG_UUID = "ARG_UUID";

    private TextToSpeech mTextToSpeech;

    private TextView mAlarmFiredTextView;
    private Button mButton;

    public static AlarmFiredFragment newInstance(UUID uuid) {
        AlarmFiredFragment fragment = new AlarmFiredFragment();
        if (uuid != null) {
            Bundle args = new Bundle();
            args.putSerializable(ARG_UUID, uuid);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_fired, container, false);

        mAlarmFiredTextView = (TextView) view.findViewById(R.id.alarm_fired_text_view);
        mButton = (Button) view.findViewById(R.id.button_talk);

        UUID uuid = (UUID) getArguments().getSerializable(ARG_UUID);
        //Alarm alarm = DatabaseAlarmController.Companion.getInstance(getActivity()).getAlarm(uuid);

        //mAlarmFiredTextView.setText("Fired alarm at time " +
        //        AlarmUtil.getHourAndMinuteAsString(alarm.getHour(), alarm.getMinute()));

        setNextAlarm();

        /*mButton.setOnClickListener(button ->
                Observable.create(new Observable.OnSubscribe<OpenMapWeather>() {
                    @Override
                    public void call(Subscriber<? super OpenMapWeather> subscriber) {
                        try {
                            subscriber.onNext(OpenMapWeatherFetchr.parseOpenMapWeather());
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            Snackbar.make(view, "Weather Error", Snackbar.LENGTH_LONG)
                                    .setAction("", view -> Log.e(TAG, "parseOpenMapWeather"))
                                    .show();
                        }
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<OpenMapWeather>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError");
                            }

                            @Override
                            public void onNext(OpenMapWeather openMapWeather) {
                                mTextToSpeech = new TextToSpeech(getActivity(), status -> {
                                    String goodMorning = "Good morning Sir, ";
                                    mTextToSpeech.speak(goodMorning + openMapWeather.toString(), TextToSpeech.QUEUE_FLUSH, null);
                                    while (mTextToSpeech.isSpeaking()) {
                                        // Just wait the end of speaking before shutdown the TextToSpeech
                                    }
                                    mTextToSpeech.stop();
                                    mTextToSpeech.shutdown();
                                });
                                Log.d(TAG, "onNext");
                            }
                        }));*/

        return view;
    }

    private void setNextAlarm() {
        /*UIAlarmController controller = UIAlarmController.getAlarmController(getActivity());
        Alarm nextAlarm = controller.getNextAlarm();
        if (nextAlarm != null) {
            AlarmFiredActivity.setActivityAlarm(getActivity(), nextAlarm.getUUID());
            Log.d(TAG, "Next alarm is " + nextAlarm.toString());
        } else {
            AlarmFiredActivity.cancelAlarm(getActivity());
            Log.d(TAG, "No other alarms");
        }*/
    }
}
