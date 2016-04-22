package com.mariotti.developer.futureclock.presenters

import com.mariotti.developer.futureclock.models.AlarmRepository
import com.mariotti.developer.futureclock.ui.activities.MainScreen
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.lang.ref.WeakReference
import java.util.*

class ListOfAlarmPresenterImpl(val mainScreen: MainScreen, val alarmRepository: AlarmRepository) :
		ListOfAlarmPresenter {

	private var subscription: Subscription? = null

	override fun loadAlarms() {
		subscription = alarmRepository.loadAlarms()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { mainScreen.showAlarms(it) }
	}

	override fun requestUpdate(alarmID: UUID) {
		mainScreen.createUpdateRequest(alarmID)
	}

	override fun release() {
		subscription?.let {
			if (!it.isUnsubscribed) it.unsubscribe()
		}
		// TODO - must MainScreen be released?
	}
}