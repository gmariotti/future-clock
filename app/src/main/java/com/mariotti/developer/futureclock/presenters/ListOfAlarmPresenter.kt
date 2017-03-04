package com.mariotti.developer.futureclock.presenters

import java.util.*

interface ListOfAlarmPresenter : BasePresenter {

	fun loadAlarms()

	fun requestUpdate(alarmID: UUID)
}