package com.mariotti.developer.futureclock.activities

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.util.Log
import com.mariotti.developer.futureclock.controllers.fragments.CreateOrUpdateAlarmFragment
import java.util.*

class AlarmCreateOrUpdateActivity : SingleFragmentActivity() {

    companion object {
        private val TAG = "AlarmCreateOrUpdateActivity"
        private val EXTRA_ALARM_UUID = "com.mariotti.developer.futureclock.activities.alarm_uuid"

        fun newIntent(packageContext: Context, alarmUUID: UUID?): Intent {
            val intent = Intent(packageContext, AlarmCreateOrUpdateActivity::class.java)

            alarmUUID?.let {
                intent.putExtra(EXTRA_ALARM_UUID, alarmUUID)
                Log.d(TAG, "UUID is $alarmUUID")
            }

            return intent
        }
    }

    override fun createFragment(): Fragment {
        val uuid = intent.getSerializableExtra(EXTRA_ALARM_UUID) as UUID?

        return CreateOrUpdateAlarmFragment.newInstance(uuid)
    }
}
