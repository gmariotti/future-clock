package com.mariotti.developer.futureclock.controller;


import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mariotti.developer.futureclock.model.OpenMapWeather;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenMapWeatherFetchr {

    private static final String TAG = "OpenMapWeatherFetchr";
    private static final String API_KEY = "250f51368f953c56b7ea9a125e25213e";
    // JSON is the default format
    private static final Uri ENDPOINT = Uri
            .parse("http://api.openweathermap.org/data/2.5/weather")
            .buildUpon()
            .appendQueryParameter("q", "Torino,IT")
            .appendQueryParameter("appid", API_KEY)
            .appendQueryParameter("units", "metric")
            .build();

    public static OpenMapWeather parseOpenMapWeather() throws IOException {
        // Create an HTTP client
        URL url = new URL(ENDPOINT.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " + ENDPOINT.toString());
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();

            String jsonResult = new String(outputStream.toByteArray());
            Log.d(TAG, "Received: " + jsonResult);

            Gson gson = new GsonBuilder().create();
            OpenMapWeather weather = gson.fromJson(jsonResult, OpenMapWeather.class);
            Log.d(TAG, "JSON weather object created");

            return weather;
        } finally {
            connection.disconnect();
        }
    }
}
