package com.mariotti.developer.futureclock.util

import android.content.Context
import java.util.*

interface ModifyOrUpdateAlarm {

    fun getActivity(): Context

    fun modifyAlarm(uuid: UUID)

    fun deleteAlarm(uuid: UUID)
}