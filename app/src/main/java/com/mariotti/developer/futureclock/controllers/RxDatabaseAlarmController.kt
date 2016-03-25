package com.mariotti.developer.futureclock.controllers

import android.content.Context
import com.mariotti.developer.futureclock.models.Alarm
import rx.Single
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Callable

class RxDatabaseAlarmController private constructor(context: Context) {

    private val mDatabaseAlarmController: DatabaseAlarmController

    init {
        mDatabaseAlarmController = DatabaseAlarmController.getInstance(context)
    }

    companion object {
        private var instance: RxDatabaseAlarmController? = null

        fun getInstance(context: Context): RxDatabaseAlarmController {
            if (instance == null) {
                instance = RxDatabaseAlarmController(context)
            }
            return instance!!
        }
    }

    @Throws(Exception::class)
    fun addAlarm(alarm: Alarm): Single<Unit> = makeSingleOnIO {
        mDatabaseAlarmController.addAlarm(alarm)
    }

    private fun <T> makeSingleOnIO(function: () -> T): Single<T> = Single.create<T> {
        it.onSuccess(Callable {
            function()
        }.call())
    }.subscribeOn(Schedulers.io())

    fun getAlarm(id: UUID): Single<Alarm?> = makeSingleOnIO {
        mDatabaseAlarmController.getAlarm(id)
    }

    @Throws(Exception::class)
    fun updateAlarm(alarm: Alarm): Single<Unit> = makeSingleOnIO {
        mDatabaseAlarmController.updateAlarm(alarm)
    }

    @Throws(Exception::class)
    fun deleteAlarm(id: UUID): Single<Unit> = makeSingleOnIO {
        mDatabaseAlarmController.deleteAlarm(id)
    }

    fun getAlarms(): Single<List<Alarm>> = makeSingleOnIO {
        mDatabaseAlarmController.getAlarms()
    }

    fun getActiveAlarms(): Single<List<Alarm>> = makeSingleOnIO {
        mDatabaseAlarmController.getActiveAlarms()
    }
}