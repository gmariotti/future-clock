package com.mariotti.developer.futureclock.extensions

import android.database.sqlite.SQLiteDatabase

inline fun SQLiteDatabase.performTransaction(dbOperations: () -> Unit) {
    this.beginTransaction()
    try {
        dbOperations()
        this.setTransactionSuccessful()
    } finally {
        this.endTransaction()
    }
}