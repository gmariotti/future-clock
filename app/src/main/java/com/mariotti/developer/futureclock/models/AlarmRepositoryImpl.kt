package com.mariotti.developer.futureclock.models

import android.content.Context
import com.mariotti.developer.futureclock.models.database.AlarmBaseHelper
import com.mariotti.developer.futureclock.models.database.AlarmCursorWrapper
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema.AlarmTable
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

class AlarmRepositoryImpl private constructor(context: Context) : AlarmRepository {

	private val briteDatabase: BriteDatabase

	init {
		val baseHelper = AlarmBaseHelper(context.applicationContext)
		val sqlBrite = SqlBrite.create()
		briteDatabase = sqlBrite.wrapDatabaseHelper(baseHelper, Schedulers.io())
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
		return briteDatabase
				.createQuery(AlarmTable.NAME, sqlQuery)
				.mapToList { cursor -> AlarmCursorWrapper(cursor).getAlarmFromDb() }
	}

	override fun loadActiveAlarms(): Observable<List<Alarm>> {
		val sqlQuery = "SELECT * FROM ${AlarmTable.NAME} WHERE active=1"
		return briteDatabase
				.createQuery(AlarmTable.NAME, sqlQuery)
				.mapToList { cursor -> AlarmCursorWrapper(cursor).getAlarmFromDb() }
	}

	override fun insertAlarm(alarm: Alarm): Observable<Unit> {
		throw UnsupportedOperationException()
	}

	override fun updateAlarm(alarm: Alarm): Observable<Unit> {
		throw UnsupportedOperationException()
	}

	override fun deleteAlarm(idAlarm: UUID): Observable<Unit> {
		throw UnsupportedOperationException()
	}

	override fun getAlarm(idAlarm: UUID): Observable<Alarm?> {
		throw UnsupportedOperationException()
	}
}