package com.mariotti.developer.futureclock.models;


public class OpenWeatherMapList {
	private int cod;
	private int count;
	private OpenWeatherMap[] list;

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public OpenWeatherMap[] getList() {
		return list;
	}

	public void setList(OpenWeatherMap[] list) {
		this.list = list;
	}
}
