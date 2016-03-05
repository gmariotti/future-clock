package com.mariotti.developer.futureclock.extensions

import android.content.res.Resources
import android.os.Build

fun Resources.getColorBasedOnApi23(colorID: Int): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return this.getColor(colorID, null)
    } else {
        return this.getColor(colorID)
    }
}