package com.mariotti.developer.futureclock.extensions

import android.database.CursorWrapper

fun CursorWrapper.getStringFromColumnName(columnName: String): String = getString(getColumnIndex(columnName))

fun CursorWrapper.getIntFromColumnName(columnName: String): Int = getInt(getColumnIndex(columnName))