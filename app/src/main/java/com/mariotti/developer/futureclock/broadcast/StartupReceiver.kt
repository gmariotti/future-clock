package com.mariotti.developer.futureclock.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StartupReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = "StartupReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
//        RxDatabaseAlarmController.getInstance(context)
        //                .getActiveAlarms()
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe {
        //                    Log.d(TAG, "StartupReceiver")
        //                    if (it.size > 0) {
        //                        val alarmToFire: Alarm? = getNextAlarm(it)
        //                        alarmToFire?.let {
        //                            makeNotificationFromAlarm(context, it)
        //
        //                            // TODO - to uncomment when FiredAlarmFragment is completed
        //                            //FiredAlarmActivity.setActivityAlarm(context, it.uuid)
        //                        }
        //                    }
        //                }
    }
}
