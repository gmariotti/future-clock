package com.mariotti.developer.futureclock.models.database

class AlarmDbSchema {
    object AlarmTable {
        val NAME = "alarms"

        object Cols {
            val UUID = "uuid"
            val TIME = "time"

            val MONDAY = "monday"
            val TUESDAY = "tuesday"
            val WEDNESDAY = "wednesday"
            val THURSDAY = "thursday"
            val FRIDAY = "friday"
            val SATURDAY = "saturday"
            val SUNDAY = "sunday"

            val TIMEZONE = "timezone"
            val ACTIVE = "active"
        }
    }
}