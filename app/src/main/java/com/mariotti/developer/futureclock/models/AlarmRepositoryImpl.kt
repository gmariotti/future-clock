package com.mariotti.developer.futureclock.models

import android.content.ContentValues
import android.content.Context
import com.mariotti.developer.futureclock.extensions.performTransaction
import com.mariotti.developer.futureclock.models.database.AlarmBaseHelper
import com.mariotti.developer.futureclock.models.database.AlarmCursorWrapper
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema.AlarmTable
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.hasDay
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

class AlarmRepositoryImpl private constructor(context: Context) : AlarmRepository {

	private val database: BriteDatabase

	init {
		val baseHelper = AlarmBaseHelper(context.applicationContext)
		val sqlBrite = SqlBrite.create()
		database = sqlBrite.wrapDatabaseHelper(baseHelper, Schedulers.io())
	}

	companion object {
		private var INSTANCE: AlarmRepositoryImpl? = null

		fun getInstance(context: Context): AlarmRepositoryImpl {
			if (INSTANCE == null) {
				INSTANCE = AlarmRepositoryImpl(context)
			}

			return INSTANCE!!
		}
	}

	override fun loadAlarms(): Observable<List<Alarm>> {
		val sqlQuery = "SELECT * FROM ${AlarmTable.NAME}"
		return database
				.createQuery(AlarmTable.NAME, sqlQuery)
				.mapToList { cursor ->
					Alarm.getAlarm(AlarmCursorWrapper(cursor).getAlarmFromDb())
				}
	}

	override fun loadActiveAlarms(): Observable<List<Alarm>> {
		val sqlQuery = "SELECT * FROM ${AlarmTable.NAME} WHERE ${AlarmTable.Cols.ACTIVE} = 1"
		return database
				.createQuery(AlarmTable.NAME, sqlQuery)
				.mapToList { cursor ->
					Alarm.getAlarm(AlarmCursorWrapper(cursor).getAlarmFromDb())
				}
	}

	override fun insertAlarm(alarm: Alarm): Observable<Unit> = Observable.fromCallable {
		val dbAlarm = getContentValues(alarm)
		database.performTransaction {
			val rowID = database.insert(AlarmTable.NAME, dbAlarm)
			if (rowID == -1L) throw Exception("Error in insertAlarm")
		}
	}.subscribeOn(Schedulers.io())

	private fun getContentValues(alarm: Alarm): ContentValues {
		val values = ContentValues()
		values.put(AlarmTable.Cols.UUID, alarm.uuid.toString())
		val time = getHourAndMinuteAsString(alarm.getHour(), alarm.getMinute())
		values.put(AlarmTable.Cols.TIME, time)

		val days = alarm.days
		values.put(AlarmTable.Cols.MONDAY, if (hasDay(days, WeekDay.MONDAY)) 1 else 0)
		values.put(AlarmTable.Cols.TUESDAY, if (hasDay(days, WeekDay.TUESDAY)) 1 else 0)
		values.put(AlarmTable.Cols.WEDNESDAY, if (hasDay(days, WeekDay.WEDNESDAY)) 1 else 0)
		values.put(AlarmTable.Cols.THURSDAY, if (hasDay(days, WeekDay.THURSDAY)) 1 else 0)
		values.put(AlarmTable.Cols.FRIDAY, if (hasDay(days, WeekDay.FRIDAY)) 1 else 0)
		values.put(AlarmTable.Cols.SATURDAY, if (hasDay(days, WeekDay.SATURDAY)) 1 else 0)
		values.put(AlarmTable.Cols.SUNDAY, if (hasDay(days, WeekDay.SUNDAY)) 1 else 0)

		values.put(AlarmTable.Cols.TIMEZONE, alarm.getTimeZone())
		values.put(AlarmTable.Cols.ACTIVE, if (alarm.active) 1 else 0)
		return values
	}

	override fun updateAlarm(alarm: Alarm): Observable<Unit> = Observable.fromCallable {
		val dbAlarm = getContentValues(alarm)
		database.performTransaction {
			val rowID = database.update(
					AlarmTable.NAME,
					dbAlarm,
					"${AlarmTable.Cols.UUID} = ?",
					alarm.uuid.toString()
			)
			if (rowID == -1) throw Exception("Error in updateAlarm")
		}
	}.subscribeOn(Schedulers.io())

	override fun deleteAlarm(idAlarm: UUID): Observable<Unit> = Observable.fromCallable {
		database.performTransaction {
			val numRowsDeleted = database.delete(
					AlarmTable.NAME, "${AlarmTable.Cols.UUID} = ?", idAlarm.toString())
			if (numRowsDeleted != 1) throw Exception("Error deleting alarm")
		}
	}.subscribeOn(Schedulers.io())

	override fun getAlarm(idAlarm: UUID): Observable<Alarm?> {
		val sqlQuery = "SELECT * FROM ${AlarmTable.NAME} WHERE ${AlarmTable.Cols.UUID}='${idAlarm.toString()}'"
		return database
				.createQuery(AlarmTable.NAME, sqlQuery)
				.mapToOne { cursor ->
					Alarm.getAlarm(AlarmCursorWrapper(cursor).getAlarmFromDb())
				}
	}
}