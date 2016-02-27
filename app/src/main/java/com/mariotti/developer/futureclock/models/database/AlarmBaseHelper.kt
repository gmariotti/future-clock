package com.mariotti.developer.futureclock.models.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AlarmBaseHelper(context: Context) : SQLiteOpenHelper(context, AlarmBaseHelper.DATABASE_NAME, null, AlarmBaseHelper.VERSION) {

    companion object {
        private val VERSION = 1
        private val DATABASE_NAME = "alarmBase.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table " + AlarmDbSchema.AlarmTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                AlarmDbSchema.AlarmTable.Cols.UUID + ", " +
                AlarmDbSchema.AlarmTable.Cols.TIME + ", " +
                AlarmDbSchema.AlarmTable.Cols.MONDAY + ", " +
                AlarmDbSchema.AlarmTable.Cols.TUESDAY + ", " +
                AlarmDbSchema.AlarmTable.Cols.WEDNESDAY + ", " +
                AlarmDbSchema.AlarmTable.Cols.THURSDAY + ", " +
                AlarmDbSchema.AlarmTable.Cols.FRIDAY + ", " +
                AlarmDbSchema.AlarmTable.Cols.SATURDAY + ", " +
                AlarmDbSchema.AlarmTable.Cols.SUNDAY + ", " +
                AlarmDbSchema.AlarmTable.Cols.ACTIVE +
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO -> manage the possibility to update the database to a new version
    }

}