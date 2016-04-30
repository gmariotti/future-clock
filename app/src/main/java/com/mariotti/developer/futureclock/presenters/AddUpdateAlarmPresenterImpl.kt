package com.mariotti.developer.futureclock.presenters

import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.AlarmRepository
import com.mariotti.developer.futureclock.ui.activities.AddUpdateAlarmScreen
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

class AddUpdateAlarmPresenterImpl(val screen: AddUpdateAlarmScreen, val repository: AlarmRepository) :
		AddUpdateAlarmPresenter {

	private var subscriptions: CompositeSubscription = CompositeSubscription()

	override fun loadAlarm(alarmID: UUID) {
		val subscription = repository.getAlarm(alarmID)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						{ alarm ->
							alarm?.let { screen.showAlarm(alarm) }
						},
						{ screen.showError(it.message) })
		subscriptions.add(subscription)
	}

	override fun addAlarm(alarm: Alarm) {
		val subscription = repository.insertAlarm(alarm)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						{ screen.confirmAddUpdate() },
						{ screen.showError(it.message) })
		subscriptions.add(subscription)
	}

	override fun updateAlarm(alarm: Alarm) {
		val subscription = repository.updateAlarm(alarm)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						{ screen.confirmAddUpdate() },
						{ screen.showError(it.message) })
		subscriptions.add(subscription)
	}

	override fun release() {
		subscriptions.clear()
	}
}