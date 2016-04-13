package com.mariotti.developer.futureclock.models

import rx.Observable
import java.util.*

interface AlarmRepository {

	@Throws(Exception::class)
	fun loadAlarms(): Observable<List<Alarm>>

	@Throws(Exception::class)
	fun loadActiveAlarms(): Observable<List<Alarm>>

	@Throws(Exception::class)
	fun insertAlarm(alarm: Alarm): Observable<Unit>

	@Throws(Exception::class)
	fun updateAlarm(alarm: Alarm): Observable<Unit>

	@Throws(Exception::class)
	fun deleteAlarm(idAlarm: UUID): Observable<Unit>

	@Throws(Exception::class)
	fun getAlarm(idAlarm: UUID): Observable<Alarm>
}