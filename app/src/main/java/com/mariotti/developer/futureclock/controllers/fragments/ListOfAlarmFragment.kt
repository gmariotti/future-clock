package com.mariotti.developer.futureclock.controllers.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.activities.AlarmCreateOrUpdateActivity
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.RxDatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.getNextAlarm
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.util.ModifyOrUpdateAlarm
import rx.Single
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class ListOfAlarmFragment : Fragment() {

    lateinit private var mAlarmFab: FloatingActionButton
    lateinit private var mAlarmRecyclerView: RecyclerView
    lateinit private var mAdapter: AlarmAdapter

    lateinit private var mAlarmToFire: Alarm
    lateinit private var mSubscription: Subscription

    companion object {
        private val TAG = "ListOfAlarmFragment"

        private val REQUEST_CODE_ALARM_MANAGEMENT = 1

        fun newInstance(): ListOfAlarmFragment {
            return ListOfAlarmFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_future_clock, container, false)

        mAlarmFab = view.findViewById(R.id.alarm_fab) as FloatingActionButton
        mAlarmFab.setOnClickListener {
            val intent = AlarmCreateOrUpdateActivity.newIntent(activity, null)
            startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT)
        }
        mAlarmRecyclerView = view.findViewById(R.id.alarms_recycler_view) as RecyclerView
        mAlarmRecyclerView.layoutManager = LinearLayoutManager(activity)
        // To avoid skipping layout because no adapter is attached
        val modifyOrUpdateAlarm = object : ModifyOrUpdateAlarm {
            private val fragment: Fragment = this@ListOfAlarmFragment

            override fun getActivity(): Context = fragment.activity

            override fun modifyAlarm(uuid: UUID) {
                val intent = AlarmCreateOrUpdateActivity.newIntent(fragment.activity, uuid)
                startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT)
            }

            override fun deleteAlarm(uuid: UUID) {
                throw UnsupportedOperationException()
            }

        }
        mAdapter = AlarmAdapter(modifyOrUpdateAlarm, arrayListOf())
        mAlarmRecyclerView.adapter = mAdapter

        updateRecyclerViewList()

        return view
    }

    private fun updateRecyclerViewList() {
        mSubscription = Single.create<List<Alarm>> {
            val alarms: List<Alarm> = DatabaseAlarmController.getInstance(context).getAlarms()
            if (!it.isUnsubscribed) {
                it.onSuccess(alarms)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { alarms ->
                    mAdapter.setAlarms(alarms)
                    mAdapter.notifyDataSetChanged()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mSubscription.isUnsubscribed) {
            mSubscription.unsubscribe()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CODE_ALARM_MANAGEMENT -> {
                updateRecyclerViewList()
                updateNextAlarmToFire()
            }
        }
    }

    private fun updateNextAlarmToFire() {
        RxDatabaseAlarmController.getInstance(context)
                .getActiveAlarms()
                .subscribe {
                    val alarmToFire = getNextAlarm(it)
                    if (alarmToFire != null && !alarmToFire.equals(mAlarmToFire)) {
                        mAlarmToFire = alarmToFire
                        // TODO - set pendingIntent
                        Log.d(TAG, alarmToFire.toShortString())
                    } else {
                        // TODO - remove pendingIntent if set because there's no active alarm
                        Log.d(TAG, "pendingIntent removed")
                    }
                }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                Snackbar.make(view!!, "Settings", Snackbar.LENGTH_LONG)
                        .show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /*override fun notifyChangedAlarm() {
        updateNextAlarmToFire()
    }*/
}
