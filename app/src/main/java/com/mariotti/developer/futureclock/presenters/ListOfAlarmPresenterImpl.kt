package com.mariotti.developer.futureclock.presenters

import com.mariotti.developer.futureclock.models.AlarmRepository
import com.mariotti.developer.futureclock.ui.fragments.MainScreen
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

class ListOfAlarmPresenterImpl(val mainScreen: MainScreen, val alarmRepository: AlarmRepository) :
		ListOfAlarmPresenter {

	private var subscription: Subscription? = null

	override fun loadAlarms() {
		subscription = alarmRepository.loadAlarms()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { mainScreen.showAlarms(it) }
	}

	override fun release() {
		subscription?.let {
			if (!it.isUnsubscribed) it.unsubscribe()
		}
		// TODO - must MainScreen be released?
	}
}