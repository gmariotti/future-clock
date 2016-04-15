package com.mariotti.developer.futureclock.ui.fragments

import com.mariotti.developer.futureclock.models.Alarm
import java.util.*

interface MainScreen {

	fun showAlarms(alarms: List<Alarm>)

	fun createUpdateRequest(alarmID: UUID? = null)
}