package com.mariotti.developer.futureclock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class FutureClockFragment extends Fragment {
    private static final String TAG = "FutureClockFragment";

    private Button mOkButton;
    private Button mVoiceButton;
    private TextView mWeatherText;

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

        return view;
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
