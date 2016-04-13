package com.mariotti.developer.futureclock.presenters

import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.AlarmRepository
import com.mariotti.developer.futureclock.presenters.AddUpdateAlarmPresenter
import com.mariotti.developer.futureclock.ui.fragments.AddUpdateAlarmScreen
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class AddUpdateAlarmPresenterImpl(val screen: AddUpdateAlarmScreen, val repository: AlarmRepository) :
		AddUpdateAlarmPresenter {

	// TODO - is correct to use a single subscription??
	private var subscription: Subscription? = null

	override fun loadAlarm(alarmID: UUID) {
		subscription =
				repository.getAlarm(alarmID)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(
								{ alarm ->
									screen.showAlarm(alarm)
								},
								{ screen.showError() }
						)
	}

	override fun addAlarm(alarm: Alarm) {
		subscription =
				repository.insertAlarm(alarm)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(
								{ screen.confirmAddUpdate() },
								{ screen.showError() }
						)

	}

	override fun updateAlarm(alarm: Alarm) {
		subscription =
				repository.updateAlarm(alarm)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(
								{ screen.confirmAddUpdate() },
								{ screen.showError() }
						)
	}

	override fun release() {
		subscription?.let { if (!it.isUnsubscribed) it.unsubscribe() }
	}
}