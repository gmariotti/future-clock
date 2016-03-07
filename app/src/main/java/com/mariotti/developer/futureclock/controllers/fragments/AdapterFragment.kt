package com.mariotti.developer.futureclock.controllers.fragments

import android.support.v4.app.Fragment
import java.util.*

abstract class AdapterFragment : Fragment() {
    abstract fun modifyAlarm(alarmUUID: UUID)
}
