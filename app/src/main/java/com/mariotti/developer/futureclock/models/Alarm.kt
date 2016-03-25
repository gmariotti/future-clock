package com.mariotti.developer.futureclock.models

import java.util.*


class Alarm(val uuid: UUID = UUID.randomUUID(), hour: Int = 0, minute: Int = 0, days: IntArray,
            val timezone: String, var active: Boolean = false) {
    val hour: Int
    val minute: Int
    val days: IntArray

    // valid only with primary constructor
    init {
        this.hour = hour % 24
        this.minute = minute % 60
        this.days = WeekDay.reorderDays(days)
    }

    constructor(uuid: UUID = UUID.randomUUID(), time: Calendar = Calendar.getInstance(),
                days: IntArray, active: Boolean = false) :
    this(uuid, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), days,
            time.timeZone.getDisplayName(false, TimeZone.SHORT), active)

    override fun toString(): String {
        val daysToString = days.joinToString { WeekDay.getShortName(it) }
        return "Alarm(uuid=$uuid, timezone='$timezone', active=$active, hour=$hour, minute=$minute, days=$daysToString)"
    }

    fun copy(uuid: UUID = this.uuid, hour: Int = this.hour, minute: Int = this.minute,
             days: IntArray = this.days, timezone: String = this.timezone,
             active: Boolean = this.active): Alarm {
        return Alarm(uuid, hour, minute, days, timezone, active)
    }

    fun toShortString(): String = "Alarm(hour=$hour, minute=$minute, " +
            "days=${days.joinToString { WeekDay.getShortName(it) }})"
}
