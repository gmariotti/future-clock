package com.mariotti.developer.futureclock;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OpenMapWeather {

    @SerializedName("weather")
    private List<Weather> mWeathers = new ArrayList<>();
    @SerializedName("main")
    private Main mMain;
    private String name;

    @Override
    public String toString() {
        return "Current weather for " + name +
                " is " + mWeathers.get(0).main + " " + mWeathers.get(0).description;
    }

    public Main getMain() {
        return mMain;
    }

    public void setMain(Main main) {
        mMain = main;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public void setWeathers(List<Weather> weathers) {
        mWeathers = weathers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private class Weather {
        private String main;
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }
    }

    private class Main {
        private Double temp;
        @SerializedName("temp_min")
        private Double tempMin;
        @SerializedName("temp_max")
        private Double tempMax;

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public Double getTempMax() {
            return tempMax;
        }

        public void setTempMax(Double tempMax) {
            this.tempMax = tempMax;
        }

        public Double getTempMin() {
            return tempMin;
        }

        public void setTempMin(Double tempMin) {
            this.tempMin = tempMin;
        }
    }
}
