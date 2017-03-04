package com.mariotti.developer.futureclock.models

import com.mariotti.developer.futureclock.functions.getDefaultTimeZone
import java.util.UUID

data class DatabaseAlarm(val uuid: UUID = UUID.randomUUID(), val hour: Int = 0, val minute: Int = 0,
												 val days: IntArray = intArrayOf(),
												 val timezone: String = getDefaultTimeZone(), var active: Boolean = false)