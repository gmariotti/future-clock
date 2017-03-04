package com.mariotti.developer.futureclock.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.models.OpenWeatherMap;
import com.mariotti.developer.futureclock.models.OpenWeatherMapList;
import com.mariotti.developer.futureclock.models.OpenWeatherMapService;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class LocationPickerPreference extends DialogPreference {
	private static final int TIMEOUT = 500;
	private static final String TAG = "LocationPicker";

	private EditText locationEditText;
	private ListView locationsListView;

	private Subscription subscription;

	public LocationPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		setDialogLayoutResource(R.layout.location_setting_dialog);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}

	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);

		locationEditText = (EditText) view.findViewById(R.id.locationSetting);
		locationsListView = (ListView) view.findViewById(R.id.locationsList);
		subscription = RxTextView.afterTextChangeEvents(locationEditText)
						.debounce(TIMEOUT, TimeUnit.MILLISECONDS)
						.flatMap(new Func1<TextViewAfterTextChangeEvent, Observable<OpenWeatherMapList>>() {
							@Override
							public Observable<OpenWeatherMapList> call(TextViewAfterTextChangeEvent textView) {
								Log.d(TAG, "flatMap");
								if (!textView.editable().toString().equals("")) {
									Retrofit.Builder builder = new Retrofit.Builder();
									Retrofit retrofit = builder
													.baseUrl("http://api.openweathermap.org/data/2.5/")
													.addConverterFactory(GsonConverterFactory.create())
													.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
													.build();

									OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
									return service.getCities(textView.editable().toString(),
													"250f51368f953c56b7ea9a125e25213e", 30);
								} else {
									return Observable.just(null);
								}
							}
						})
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Subscriber<OpenWeatherMapList>() {
							@Override
							public void onCompleted() {
								Log.d(TAG, "onCompleted");
							}

							@Override
							public void onError(Throwable e) {
								Log.d(TAG, "onError");
							}

							@Override
							public void onNext(OpenWeatherMapList openWeatherMapList) {
								Log.d(TAG, "onNext");
								String[] listOfCities;
								if (openWeatherMapList != null) {
									OpenWeatherMap[] list = openWeatherMapList.getList();
									listOfCities = new String[openWeatherMapList.getCount()];
									for (int i = 0; i < list.length; i++) {
										listOfCities[i] = list[i].getName() + ", " + list[i].getSys().getCountry();
									}
								} else {
									listOfCities = new String[0];
								}
								locationsListView.setAdapter(new ArrayAdapter<>(getContext(), android.R
												.layout.simple_list_item_1, listOfCities));
							}
						});
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);

		if (!subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}
}
