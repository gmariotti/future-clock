package com.mariotti.developer.futureclock.presenters

import com.mariotti.developer.futureclock.models.AlarmRepository
import com.mariotti.developer.futureclock.ui.activities.MainScreen
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import java.util.UUID

class ListOfAlarmPresenterImpl(val mainScreen: MainScreen, val alarmRepository: AlarmRepository) :
				ListOfAlarmPresenter {

	private var subscriptions: CompositeSubscription = CompositeSubscription()

	override fun loadAlarms() {
		val subscription = alarmRepository.loadAlarms()
						.map { it.sorted() }
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe { mainScreen.showAlarms(it) }
		subscriptions.add(subscription)
	}

	override fun requestUpdate(alarmID: UUID) {
		mainScreen.createUpdateRequest(alarmID)
	}

	override fun release() {
		subscriptions.clear()
	}
}