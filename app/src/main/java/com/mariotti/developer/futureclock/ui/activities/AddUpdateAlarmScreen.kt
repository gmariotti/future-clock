package com.mariotti.developer.futureclock.ui.activities

import com.mariotti.developer.futureclock.models.Alarm

interface AddUpdateAlarmScreen {

	fun showAlarm(alarm: Alarm)

	fun confirmAddUpdate()

	fun updateTime(hourOfDay: Int, minute: Int)

	fun showError(message: String?)
}