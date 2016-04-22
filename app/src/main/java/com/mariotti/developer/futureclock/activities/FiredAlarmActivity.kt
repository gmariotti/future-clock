package com.mariotti.developer.futureclock.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import com.mariotti.developer.futureclock.controllers.fragments.FiredAlarmFragment
import com.mariotti.developer.futureclock.controllers.getNearestDayForAlarm
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.ui.activities.MainActivity
import com.mariotti.developer.futureclock.ui.activities.BaseActivity
import java.util.*

class FiredAlarmActivity : BaseActivity() {

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

		fun setActiveAlarm(context: Context, uuid: UUID) {
//			RxDatabaseAlarmController.getInstance(context)
			//					.getAlarm(uuid)
			//					.observeOn(AndroidSchedulers.mainThread())
			//					.subscribe(object : SingleSubscriber<Alarm?>() {
			//						override fun onSuccess(alarmFound: Alarm?) {
			//							Log.d(TAG, "onSuccess")
			//							alarmFound?.let {
			//								Log.d(TAG, "an alarm has been found")
			//								setPendingIntentForAlarm(context, it)
			//							}
			//						}
			//
			//						override fun onError(error: Throwable) {
			//							Log.d(TAG, "Error in finding alarm - " + uuid.toString())
			//						}
			//					})
		}

		private fun setPendingIntentForAlarm(context: Context, alarm: Alarm) {
			val pendingIntent = getPendingIntentForActivity(context, alarm.uuid)
			val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			val alarmTimeInMillis = getNearestDayForAlarm(alarm, Calendar.getInstance())
					.timeInMillis

			// info of the pendingIntent -> they will redirect to the list of alarms
			val pendingIntentAlarmInfo = getPendingIntentForAlarmInfo(context)
			val clockInfo = AlarmManager.AlarmClockInfo(alarmTimeInMillis, pendingIntentAlarmInfo)
			alarmManager.setAlarmClock(clockInfo, pendingIntent)
		}

		private fun getPendingIntentForActivity(context: Context, uuid: UUID): PendingIntent {
			val intent = FiredAlarmActivity.newIntent(context)
			intent.putExtra(EXTRA_ALARM_FIRED_UUID, uuid)

			return PendingIntent.getActivity(context, REQUEST_CODE,
					intent, PendingIntent.FLAG_CANCEL_CURRENT)
		}

		private fun getPendingIntentForAlarmInfo(context: Context): PendingIntent {
			val intentAlarmInfo = MainActivity.newIntent(context)
			return PendingIntent.getActivity(context, REQUEST_CODE, intentAlarmInfo,
					PendingIntent.FLAG_CANCEL_CURRENT)
		}

		fun removeAlarmIfSet(context: Context) {
			val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			if (alarmManager.nextAlarmClock != null) {
				cancelAlarm(context)
				Log.d(TAG, "Alarm To Fire Removed")
			} else {
				Log.d(TAG, "No Alarm was set")
			}
		}

		private fun cancelAlarm(context: Context) {
			val intent = FiredAlarmActivity.newIntent(context)
			val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, intent, 0)
			val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

			alarmManager.cancel(pendingIntent)
			Log.d(TAG, "PendingIntent deleted")
		}
	}
}
