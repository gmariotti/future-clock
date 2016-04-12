package com.mariotti.developer.futureclock.controllers.fragments

import android.content.Context
import java.util.*

interface ModifyOrUpdateAlarm {

    fun getActivity(): Context

    fun modifyAlarm(uuid: UUID)

    fun deleteAlarm(uuid: UUID)
}