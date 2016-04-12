package com.mariotti.developer.futureclock.models

import rx.Observable
import java.util.*

interface AlarmRepository {

	fun loadAlarms(): Observable<List<Alarm>>

	fun loadActiveAlarms(): Observable<List<Alarm>>

	fun insertAlarm(alarm: Alarm): Observable<Unit>

	fun updateAlarm(alarm: Alarm): Observable<Unit>

	fun deleteAlarm(idAlarm: UUID): Observable<Unit>

	fun getAlarm(idAlarm: UUID): Observable<Alarm?>
}