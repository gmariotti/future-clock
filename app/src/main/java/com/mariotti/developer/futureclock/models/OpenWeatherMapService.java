package com.mariotti.developer.futureclock.models;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OpenWeatherMapService {
	@GET("weather")
	Observable<OpenWeatherMap> getWeather(@Query("q") String location, @Query("appid") String api_key,
	                                      @Query("units") String metric);

	@GET("find")
	Observable<OpenWeatherMapList> getCities(@Query("q") String city, @Query("appid") String api_key,
	                                   @Query("cnt") int count);
}
