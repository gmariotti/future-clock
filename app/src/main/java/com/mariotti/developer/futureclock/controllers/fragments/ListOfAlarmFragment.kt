package com.mariotti.developer.futureclock.controllers.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.activities.AlarmCreateOrUpdateActivity
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class ListOfAlarmFragment : AdapterFragment() {

    private var mAlarmFab: FloatingActionButton? = null
    private var mAlarmRecyclerView: RecyclerView? = null
    private var mAdapter: AlarmAdapter? = null
    private var mSubscription: Subscription? = null

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_future_clock, container, false)

        mAlarmFab = view.findViewById(R.id.alarm_fab) as FloatingActionButton
        mAlarmFab!!.setOnClickListener { v ->
            val intent = AlarmCreateOrUpdateActivity.newIntent(activity, null)
            startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT)
        }
        mAlarmRecyclerView = view.findViewById(R.id.alarms_recycler_view) as RecyclerView
        mAlarmRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        // To avoid skipping layout because no adapter is attached
        mAdapter = AlarmAdapter(this, arrayListOf())
        mAlarmRecyclerView!!.adapter = mAdapter

        updateRecyclerViewList()

        return view
    }

    private fun updateRecyclerViewList() {
        mSubscription = DatabaseAlarmController.getInstance(activity)
                .getAlarms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { alarms ->
                    mAdapter!!.setAlarms(alarms)
                    mAdapter!!.notifyDataSetChanged()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscription?.let {
            if (!it.isUnsubscribed) {
                it.unsubscribe()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CODE_ALARM_MANAGEMENT -> updateRecyclerViewList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
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

    override fun modifyAlarm(alarmUUID: UUID) {
        val intent = AlarmCreateOrUpdateActivity.newIntent(activity, alarmUUID)
        startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT)
    }
}
