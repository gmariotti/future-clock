package com.mariotti.developer.futureclock.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMap {

	private List<Weather> weathers = new ArrayList<>();
	private Main main;
	private Sys sys;
	private String id;
	private String name;

	@Override
	public String toString() {
		return "Current weather for " + name +
						" is " + weathers.get(0).main + " " + weathers.get(0).description;
	}

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public List<Weather> getWeathers() {
		return weathers;
	}

	public void setWeathers(List<Weather> weathers) {
		this.weathers = weathers;
	}

	public Sys getSys() {
		return sys;
	}

	public void setSys(Sys sys) {
		this.sys = sys;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public class Main {
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

	public class Sys {
		private String country;

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}
	}

}
