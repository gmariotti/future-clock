package com.mariotti.developer.futureclock.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.getShortDaysString
import java.util.*

class ListOfAlarmAdapter(private var alarms: List<Alarm>) : RecyclerView.Adapter<ListOfAlarmAdapter.AlarmHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val view = layoutInflater.inflate(R.layout.list_item_alarm, parent, false)

		return AlarmHolder(view, this)
	}

	override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
		val alarm = alarms[position]
		holder.bindAlarm(alarm, position)
	}

	override fun getItemCount(): Int {
		return alarms.size
	}

	fun setAlarms(alarms: List<Alarm>) {
		this.alarms = alarms
		this.notifyDataSetChanged()
	}

	fun modifyAlarm(uuid: UUID) {
		//mModifyOrUpdateAlarm.modifyAlarm(uuid)
	}

	fun deleteAlarm(uuid: UUID) {
		//mModifyOrUpdateAlarm.deleteAlarm(uuid)
	}

	fun updateAlarmInList(alarm: Alarm, position: Int): Unit {
		setAlarms(alarms.mapIndexed { index, listAlarm ->
			if (index == position) alarm else listAlarm
		})
		//mFragment.notifyChangedAlarm()
	}

	inner class AlarmHolder(itemView: View, private val mAdapter: ListOfAlarmAdapter) :
			RecyclerView.ViewHolder(itemView), View.OnClickListener {

		private val mTimeTextView: TextView
		private val mDaysTextView: TextView
		private val mActiveSwitch: Switch

		lateinit private var mAlarm: Alarm
		private var mPosition: Int

		init {
			itemView.setOnClickListener(this)

			mTimeTextView = itemView.findViewById(R.id.list_item_time) as TextView
			mDaysTextView = itemView.findViewById(R.id.list_item_days) as TextView
			mActiveSwitch = itemView.findViewById(R.id.list_item_switch) as Switch
			mPosition = -1
		}

		fun bindAlarm(alarm: Alarm, position: Int) {
			mAlarm = alarm
			mPosition = position

			mTimeTextView.text = getHourAndMinuteAsString(alarm.hour, alarm.minute)
			mDaysTextView.text = getShortDaysString(alarm)
			mActiveSwitch.isChecked = alarm.active

			mActiveSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
				// TODO
			}

			mActiveSwitch.isEnabled = false
		}

		override fun onClick(v: View) {
			mAdapter.modifyAlarm(mAlarm.uuid)
		}
	}
}