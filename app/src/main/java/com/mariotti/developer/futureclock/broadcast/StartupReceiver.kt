package com.mariotti.developer.futureclock.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.controllers.RxDatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.getNextAlarm
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.getShortDaysString
import com.mariotti.developer.futureclock.util.makeNotificationFromAlarm
import rx.android.schedulers.AndroidSchedulers

class StartupReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = "StartupReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        RxDatabaseAlarmController.getInstance(context)
                .getActiveAlarms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d(TAG, "StartupReceiver")
                    if (it.size > 0) {
                        val alarmToFire: Alarm? = getNextAlarm(it)
                        alarmToFire?.let {
                            makeNotificationFromAlarm(context, it)

                            // TODO - to uncomment when FiredAlarmFragment is completed
                            //FiredAlarmActivity.setActivityAlarm(context, it.uuid)
                        }
                    }
                }
    }
}
