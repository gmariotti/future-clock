package com.mariotti.developer.futureclock.models

import java.util.*

data class DatabaseAlarm(val uuid: UUID = UUID.randomUUID(), val hour: Int = 0, val minute: Int = 0,
                         val days: IntArray = intArrayOf(),
                         val timezone: String = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT),
                         var active: Boolean = false)

class Alarm(val uuid: UUID = UUID.randomUUID(),
            val time: HourMinuteAndTimeZone = HourMinuteAndTimeZone.getHourMinuteAndTimeZone(),
            days: IntArray = intArrayOf(), val active: Boolean = false) {
	val days: IntArray

	init {
		this.days = WeekDay.reorderDays(days)
	}

	companion object {
		fun getDatabaseAlarm(alarm: Alarm): DatabaseAlarm {
			val (hour, minute, timezone) = alarm.time
			return DatabaseAlarm(alarm.uuid, hour, minute, alarm.days, timezone, alarm.active)
		}

		fun getAlarm(dbAlarm: DatabaseAlarm): Alarm {
			val time = HourMinuteAndTimeZone
					.getHourMinuteAndTimeZone(dbAlarm.hour, dbAlarm.minute, dbAlarm.timezone)
			return Alarm(dbAlarm.uuid, time, dbAlarm.days, dbAlarm.active)
		}
	}

	fun getHour(): Int = time.hour

	fun getMinute(): Int = time.minute

	fun getTimeZone(): String = time.timezone

	fun copy(uuid: UUID = this.uuid,
	         time: HourMinuteAndTimeZone = HourMinuteAndTimeZone.getHourMinuteAndTimeZone(),
	         days: IntArray = this.days, active: Boolean = this.active): Alarm = Alarm(uuid, time, days, active)

	override fun equals(other: Any?): Boolean {
		when (other) {
			is Alarm ->
				if (other.uuid != this.uuid || other.active != this.active ||
						other.days != this.days || other.time != this.time) return false
				else return true
			else -> return false
		}
	}

	override fun hashCode(): Int {
		return super.hashCode()
	}
}
