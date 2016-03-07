package com.mariotti.developer.futureclock.extensions

import android.database.sqlite.SQLiteDatabase

fun SQLiteDatabase.performTransaction(body: () -> Unit) {
    this.beginTransaction()
    try {
        body()
        this.setTransactionSuccessful()
    } finally {
        this.endTransaction()
    }
}