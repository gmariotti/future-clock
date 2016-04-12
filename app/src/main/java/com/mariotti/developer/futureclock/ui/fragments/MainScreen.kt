package com.mariotti.developer.futureclock.ui.fragments

import com.mariotti.developer.futureclock.models.Alarm

interface MainScreen {

	fun showAlarms(alarms: List<Alarm>)
}