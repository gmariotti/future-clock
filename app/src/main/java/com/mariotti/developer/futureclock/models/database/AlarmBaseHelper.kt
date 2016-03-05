package com.mariotti.developer.futureclock.models.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mariotti.developer.futureclock.models.database.AlarmDbSchema.AlarmTable

class AlarmBaseHelper(context: Context) : SQLiteOpenHelper(context, AlarmBaseHelper.DATABASE_NAME, null, AlarmBaseHelper.VERSION) {

    companion object {
        private val VERSION = 1
        private val DATABASE_NAME = "alarmBase.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table ${AlarmTable.NAME}(_id integer primary key autoincrement, " +
                "${AlarmTable.Cols.UUID}, ${AlarmTable.Cols.TIME}, ${AlarmTable.Cols.MONDAY}, " +
                "${AlarmTable.Cols.TUESDAY}, ${AlarmTable.Cols.WEDNESDAY}, ${AlarmTable.Cols.THURSDAY}, " +
                "${AlarmTable.Cols.FRIDAY}, ${AlarmTable.Cols.SATURDAY}, ${AlarmTable.Cols.SUNDAY}, " +
                "${AlarmTable.Cols.TIMEZONE}, ${AlarmTable.Cols.ACTIVE})"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO -> manage the possibility to update the database to a new version
    }

}