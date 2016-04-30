package com.mariotti.developer.futureclock.models

import com.mariotti.developer.futureclock.functions.getDefaultTimeZone
import java.util.Arrays
import java.util.UUID

class Alarm(val uuid: UUID = UUID.randomUUID(),
            val time: HourMinuteAndTimeZone = HourMinuteAndTimeZone.getFromCalendar(),
            _days: IntArray = intArrayOf(), val active: Boolean = false) : Comparable<Alarm> {
	val days: IntArray

	init {
		days = WeekDay.reorderDays(_days)
	}

	companion object {

		fun getDatabaseAlarm(alarm: Alarm): DatabaseAlarm {
			val (hour, minute, timezone) = alarm.time
			return DatabaseAlarm(alarm.uuid, hour, minute, alarm.days, timezone, alarm.active)
		}

		fun getAlarm(dbAlarm: DatabaseAlarm): Alarm {
			val time = HourMinuteAndTimeZone
							.getFromVariables(dbAlarm.hour, dbAlarm.minute, dbAlarm.timezone)
			return Alarm(dbAlarm.uuid, time, dbAlarm.days, dbAlarm.active)
		}

	}

	fun getHour() = time.hour

	fun getMinute() = time.minute

	fun getTimeZone() = time.timezone

	fun copy(uuid: UUID = this.uuid,
	         time: HourMinuteAndTimeZone = HourMinuteAndTimeZone.getFromCalendar(),
	         days: IntArray = this.days, active: Boolean = this.active) = Alarm(uuid, time, days, active)

	override fun equals(other: Any?): Boolean {
		other?.let {
			when (other) {
				is Alarm -> {
					if (other.uuid != this.uuid || other.active != this.active ||
									!Arrays.equals(other.days, this.days) || other.time != this.time) return false
					else return true
				}
				else -> return false
			}
		}

		return false
	}

	override fun hashCode() = super.hashCode()

	override fun compareTo(other: Alarm): Int {
		val hourDiff = this.getHour() - other.getHour()
		return if (hourDiff != 0) hourDiff else this.getMinute() - other.getMinute()
	}
}
