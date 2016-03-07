package com.mariotti.developer.futureclock.controllers.fragments

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.getShortDaysString
import rx.SingleSubscriber
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers

class AlarmAdapter(private val mFragment: AdapterFragment, private var mAlarms: List<Alarm>) :
        RecyclerView.Adapter<AlarmAdapter.AlarmHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val layoutInflater = LayoutInflater.from(mFragment.activity)
        val view = layoutInflater.inflate(R.layout.list_item_alarm, parent, false)

        return AlarmHolder(view, this)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        val alarm = mAlarms[position]
        holder.bindAlarm(alarm)
    }

    override fun getItemCount(): Int {
        return mAlarms.size
    }

    fun setAlarms(alarms: List<Alarm>) {
        mAlarms = alarms
    }

    inner class AlarmHolder(itemView: View, private val mAdapter: AlarmAdapter) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val mTimeTextView: TextView
        private val mDaysTextView: TextView
        private val mActiveSwitch: Switch

        private var mAlarm: Alarm? = null

        init {
            itemView.setOnClickListener(this)

            mTimeTextView = itemView.findViewById(R.id.list_item_time) as TextView
            mDaysTextView = itemView.findViewById(R.id.list_item_days) as TextView
            mActiveSwitch = itemView.findViewById(R.id.list_item_switch) as Switch
        }

        fun bindAlarm(alarm: Alarm) {
            mAlarm = alarm
            mTimeTextView.text = getHourAndMinuteAsString(alarm.hour, alarm.minute)
            mDaysTextView.text = getShortDaysString(alarm)
            mActiveSwitch.isChecked = alarm.active

            mActiveSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                mAlarm!!.active = isChecked
                DatabaseAlarmController.getInstance(mFragment.activity)
                        .updateAlarm(mAlarm!!)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleSubscriber<Unit>() {
                            override fun onSuccess(p0: Unit?) {
                                Log.d("AlarmHolder[$adapterPosition]", "onSuccess")
                            }

                            override fun onError(p0: Throwable?) {
                                Log.d("AlarmHolder[$adapterPosition]", "Error in enable/disable alarm")
                            }

                        })
            }
        }

        override fun onClick(v: View) {
            mAdapter.mFragment.modifyAlarm(mAlarm!!.uuid)
        }
    }
}