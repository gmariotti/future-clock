package com.mariotti.developer.futureclock.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.getNextAlarm
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.getShortDaysString
import rx.android.schedulers.AndroidSchedulers

class StartupReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = "StartupReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        DatabaseAlarmController.getInstance(context)
                .getActiveAlarms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d(TAG, "StartupReceiver")
                    if (it.size > 0) {
                        val alarmToFire: Alarm? = getNextAlarm(it)
                        alarmToFire?.let {
                            val mBuilder: NotificationCompat.Builder =
                                    NotificationCompat.Builder(context)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("WhatsUpClock")
                                            .setContentText(
                                                    "${getHourAndMinuteAsString(it.hour, it.minute)} - " +
                                                            "${getShortDaysString(it)}"
                                            )
                            val notificationManager: NotificationManager =
                                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.notify(10, mBuilder.build())
                        }
                    }
                }
    }
}
