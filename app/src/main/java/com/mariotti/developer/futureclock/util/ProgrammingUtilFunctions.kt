package com.mariotti.developer.futureclock.util

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.models.Alarm

fun makeNotificationFromAlarm(context: Context, alarm: Alarm) {
    val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("WhatsUpClock")
                    .setContentText(
                            "${getHourAndMinuteAsString(alarm.hour, alarm.minute)} - " +
                                    "${getShortDaysString(alarm)}"
                    )
    val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(10, mBuilder.build())
}