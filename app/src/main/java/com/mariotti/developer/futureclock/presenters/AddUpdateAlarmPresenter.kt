package com.mariotti.developer.futureclock.presenters

import com.mariotti.developer.futureclock.models.Alarm
import java.util.*

interface AddUpdateAlarmPresenter : BasePresenter {

	fun loadAlarm(alarmID: UUID)

	fun addAlarm(alarm: Alarm)

	fun updateAlarm(alarm: Alarm)
}