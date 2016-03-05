package com.mariotti.developer.futureclock.controllers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.WeekDay.FRIDAY
import com.mariotti.developer.futureclock.models.WeekDay.MONDAY
import com.mariotti.developer.futureclock.models.WeekDay.SATURDAY
import com.mariotti.developer.futureclock.models.WeekDay.SUNDAY
import com.mariotti.developer.futureclock.models.WeekDay.THURSDAY
import com.mariotti.developer.futureclock.models.WeekDay.TUESDAY
import com.mariotti.developer.futureclock.models.WeekDay.WEDNESDAY
import com.mariotti.developer.futureclock.models.database.AlarmBaseHelper
import com.mariotti.developer.futureclock.models.database.AlarmCursorWrapper
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema.AlarmTable
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.hasDay
import rx.Observable
import java.util.*
import java.util.concurrent.Callable

class DatabaseAlarmController private constructor(context: Context) {

    private val mDatabase: SQLiteDatabase

    init {
        mDatabase = AlarmBaseHelper(context.applicationContext).writableDatabase
    }

    companion object {
        private var instance: DatabaseAlarmController? = null

        fun getInstance(context: Context): DatabaseAlarmController {
            if (instance == null) {
                instance = DatabaseAlarmController(context)
            }

            return instance!!
        }
    }

    @Throws(Exception::class)
    fun addAlarm(alarm: Alarm): Observable<Unit> = Observable.create<Unit> {
        it.onNext(Callable<Unit> {
            val values = getContentValues(alarm)

            // TODO - convert to SQLiteDatabase function with lambda
            mDatabase.beginTransaction()
            try {
                val rowID = mDatabase.insert(AlarmTable.NAME, null, values)
                if (rowID == -1L) {
                    throw Exception("Error inserting alarm")
                }
                mDatabase.setTransactionSuccessful()
            } finally {
                mDatabase.endTransaction()
            }
        }.call())
    }

    private fun getContentValues(alarm: Alarm): ContentValues {
        val values = ContentValues()
        values.put(AlarmTable.Cols.UUID, alarm.uuid.toString())
        val time = getHourAndMinuteAsString(alarm.hour, alarm.minute)
        values.put(AlarmTable.Cols.TIME, time)

        val days = alarm.days
        values.put(AlarmTable.Cols.MONDAY, if (hasDay(days, MONDAY)) 1 else 0)
        values.put(AlarmTable.Cols.TUESDAY, if (hasDay(days, TUESDAY)) 1 else 0)
        values.put(AlarmTable.Cols.WEDNESDAY, if (hasDay(days, WEDNESDAY)) 1 else 0)
        values.put(AlarmTable.Cols.THURSDAY, if (hasDay(days, THURSDAY)) 1 else 0)
        values.put(AlarmTable.Cols.FRIDAY, if (hasDay(days, FRIDAY)) 1 else 0)
        values.put(AlarmTable.Cols.SATURDAY, if (hasDay(days, SATURDAY)) 1 else 0)
        values.put(AlarmTable.Cols.SUNDAY, if (hasDay(days, SUNDAY)) 1 else 0)

        values.put(AlarmTable.Cols.TIMEZONE, alarm.timezone)
        values.put(AlarmTable.Cols.ACTIVE, if (alarm.active) 1 else 0)
        return values
    }

    fun getAlarm(id: UUID): Observable<Alarm?> = Observable.create {
        it.onNext(Callable<Alarm?> {
            val cursorWrapper = queryAlarms(
                    AlarmTable.Cols.UUID + " = ?",
                    arrayOf(id.toString())
            )
            try {
                if (cursorWrapper.count == 0) {
                    null
                }

                cursorWrapper.moveToFirst()
                cursorWrapper.getAlarmFromDb()
            } finally {
                cursorWrapper.close()
            }
        }.call())
    }

    private fun queryAlarms(whereClause: String?, whereArgs: Array<String>?): AlarmCursorWrapper {
        val cursor = mDatabase.query(
                AlarmTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null)

        return AlarmCursorWrapper(cursor)
    }

    @Throws(Exception::class)
    fun updateAlarm(alarm: Alarm): Observable<Unit> = Observable.create {
        it.onNext(Callable<Unit> {

            val uuidString = alarm.uuid.toString()
            val values = getContentValues(alarm)

            // TODO - convert to SQLiteDatabase function with lambda
            with(mDatabase) {
                beginTransaction()
                try {
                    val numRowsModified = update(
                            AlarmTable.NAME,
                            values,
                            AlarmTable.Cols.UUID + " = ?",
                            arrayOf(uuidString))
                    if (numRowsModified != 1) {
                        throw Exception("Error updating alarm " + uuidString)
                    }
                    setTransactionSuccessful()
                } finally {
                    endTransaction()
                }
            }
        }.call())
    }

    @Throws(Exception::class)
    fun deleteAlarm(uuid: UUID): Observable<Unit> = Observable.create {
        it.onNext(Callable<Unit> {

            // TODO - convert to SQLiteDatabase function with lambda
            with(mDatabase) {
                beginTransaction()
                try {
                    val numRowsDeleted = delete(
                            AlarmTable.NAME,
                            AlarmTable.Cols.UUID + " = ?",
                            arrayOf(uuid.toString()))
                    if (numRowsDeleted != 1) {
                        throw Exception("Error delete alarm " + uuid.toString())
                    }
                    setTransactionSuccessful()
                } finally {
                    endTransaction()
                }
            }
        }.call())
    }

    fun getAlarms(): Observable<List<Alarm>> = Observable.create {
        it.onNext(Callable<List<Alarm>> {
            val cursorWrapper = queryAlarms(null, null)
            val alarms = createListFromCursorWrapper(cursorWrapper)
            alarms
        }.call())
    }

    private fun createListFromCursorWrapper(cursorWrapper: AlarmCursorWrapper): List<Alarm> {
        val alarms = ArrayList<Alarm>()

        try {
            cursorWrapper.moveToFirst()
            while (!cursorWrapper.isAfterLast) {
                alarms.add(cursorWrapper.getAlarmFromDb())
                cursorWrapper.moveToNext()
            }
        } finally {
            cursorWrapper.close()
        }

        return alarms
    }

    fun getActiveAlarms(): Observable<List<Alarm>> = Observable.create {
        it.onNext(Callable<List<Alarm>> {
            val cursorWrapper = queryAlarms(
                    AlarmTable.Cols.ACTIVE + " = 1",
                    null)
            val alarms = createListFromCursorWrapper(cursorWrapper)
            alarms
        }.call())
    }
}