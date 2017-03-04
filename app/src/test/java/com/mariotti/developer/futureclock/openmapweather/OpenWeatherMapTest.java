package com.mariotti.developer.futureclock.openmapweather;


import com.mariotti.developer.futureclock.models.OpenWeatherMapList;
import com.mariotti.developer.futureclock.models.OpenWeatherMapService;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscription;

import static org.junit.Assert.assertEquals;

public class OpenWeatherMapTest {

	@Test
	public void ListOfCitiesWithSpringfieldName() throws IOException {
		Retrofit.Builder builder = new Retrofit.Builder();
		Retrofit retrofit = builder
						.baseUrl("http://api.openweathermap.org/data/2.5/")
						.addConverterFactory(GsonConverterFactory.create())
						.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
						.build();

		OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
		Subscription subscription = service
						.getCities("Springfield", "250f51368f953c56b7ea9a125e25213e", 30)
						.subscribe(new Observer<OpenWeatherMapList>() {
							@Override
							public void onCompleted() {

							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onNext(OpenWeatherMapList openWeatherMapList) {
								assertEquals(openWeatherMapList.getCod(), 200);
								assertEquals(openWeatherMapList.getCount(), 8);
							}
						});
	}
}
