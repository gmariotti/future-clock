package com.mariotti.developer.futureclock.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.RxDatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.fragments.FiredAlarmFragment
import com.mariotti.developer.futureclock.controllers.getNearestDayForAlarm
import com.mariotti.developer.futureclock.models.Alarm
import rx.SingleSubscriber
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class FiredAlarmActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        val uuid = intent.getSerializableExtra(EXTRA_ALARM_FIRED_UUID) as UUID
        Log.d(TAG, "UUID = " + uuid.toString())

        return FiredAlarmFragment.newInstance(uuid)
    }

    companion object {
        private val EXTRA_ALARM_FIRED_UUID = "com.mariotti.developer.futureclock.activities.FiredAlarmActivity"
        private val TAG = "FiredAlarmActivity"
        private val REQUEST_CODE = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, FiredAlarmActivity::class.java)
        }

        fun setActivityAlarm(context: Context, uuid: UUID) {
            RxDatabaseAlarmController.getInstance(context)
                    .getAlarm(uuid)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleSubscriber<Alarm?>() {
                        override fun onSuccess(alarmFound: Alarm?) {
                            alarmFound?.let {
                                val pendingIntent = getPendingIntentForActivity(context, uuid)
                                val alarmManager =
                                        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                val alarmTime = getNearestDayForAlarm(it, Calendar.getInstance())
                                        .timeInMillis
                                val pendingIntentAlarmInfo = getPendingIntentForAlarmInfo(context)
                                val clockInfo = AlarmManager.AlarmClockInfo(alarmTime, pendingIntentAlarmInfo)
                                alarmManager.setAlarmClock(clockInfo, pendingIntent)
                            }
                        }

                        override fun onError(error: Throwable) {
                            Log.d(TAG, "Error in finding alarm - " + uuid.toString())
                        }
                    })
        }

        private fun getPendingIntentForActivity(context: Context, uuid: UUID): PendingIntent {
            val intent = FiredAlarmActivity.newIntent(context)
            intent.putExtra(EXTRA_ALARM_FIRED_UUID, uuid)
            return PendingIntent.getActivity(context, REQUEST_CODE,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        private fun getPendingIntentForAlarmInfo(context: Context): PendingIntent {
            val intentAlarmInfo = ListOfAlarmActivity.newIntent(context)
            return PendingIntent.getActivity(context, REQUEST_CODE,
                    intentAlarmInfo, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        fun cancelAlarm(context: Context) {
            val intent = FiredAlarmActivity.newIntent(context)
            val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            Log.d(TAG, "PendingIntent deleted")
        }
    }
}
