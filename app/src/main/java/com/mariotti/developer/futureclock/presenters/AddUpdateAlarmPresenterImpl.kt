package com.mariotti.developer.futureclock.presenters

import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.AlarmRepository
import com.mariotti.developer.futureclock.ui.activities.AddUpdateAlarmScreen
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class AddUpdateAlarmPresenterImpl(val screen: AddUpdateAlarmScreen, val repository: AlarmRepository) :
		AddUpdateAlarmPresenter {

	private var subscription: Subscription? = null

	override fun loadAlarm(alarmID: UUID) {
		subscription =
				repository.getAlarm(alarmID)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(
								{ alarm ->
									alarm?.let { screen.showAlarm(alarm) }
								},
								{ screen.showError(it.message) }
						)
	}

	override fun addAlarm(alarm: Alarm) {
		subscription =
				repository.insertAlarm(alarm)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(
								{ screen.confirmAddUpdate() },
								{ screen.showError(it.message) }
						)
	}

	override fun updateAlarm(alarm: Alarm) {
		subscription =
				repository.updateAlarm(alarm)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(
								{ screen.confirmAddUpdate() },
								{ screen.showError(it.message) }
						)
	}

	override fun release() {
		subscription?.let { if (!it.isUnsubscribed) it.unsubscribe() }
	}
}