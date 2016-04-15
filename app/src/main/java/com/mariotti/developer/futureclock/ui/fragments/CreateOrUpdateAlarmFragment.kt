package com.mariotti.developer.futureclock.ui.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.extensions.getColorBasedOnApi23
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.AlarmRepositoryImpl
import com.mariotti.developer.futureclock.models.WeekDay
import com.mariotti.developer.futureclock.presenters.AddUpdateAlarmPresenter
import com.mariotti.developer.futureclock.presenters.AddUpdateAlarmPresenterImpl
import com.mariotti.developer.futureclock.util.addDay
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.hasDay
import com.mariotti.developer.futureclock.util.removeDay
import java.util.*

class CreateOrUpdateAlarmFragment : Fragment(), AddUpdateAlarmScreen {

	private var alarmID: UUID = UUID.randomUUID()
	private var alarmHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
	private var alarmMinute: Int = Calendar.getInstance().get(Calendar.MINUTE)
	private var alarmDays: IntArray = intArrayOf()
	private var alarmIsActive: Boolean = false
	private var isUpdateOperation: Boolean = false

	lateinit private var timeTextView: TextView
	lateinit private var daysTextView: Array<TextView>
	lateinit private var activeSwitch: Switch
	lateinit private var confirmButton: Button
	private val presenter: AddUpdateAlarmPresenter by lazy {
		AddUpdateAlarmPresenterImpl(this, AlarmRepositoryImpl.getInstance(activity.applicationContext))
	}

	companion object {
		private val TAG = "CreateOrUpdateAlarmFragment"
		private val DIALOG_TIME = "TimePickerFragment"
		private val UUID_ARG = "UUID_ARG"
		private val REQUEST_CODE_TIME = 1

		fun newInstance(uuid: UUID?): CreateOrUpdateAlarmFragment {
			val fragment = CreateOrUpdateAlarmFragment()
			uuid?.let {
				val args = Bundle()
				args.putSerializable(UUID_ARG, uuid)
				fragment.arguments = args
			}

			return fragment
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_alarm, container, false)

		initNoAlarmDependencies(view)
		arguments?.let {
			val alarmUUID = arguments.getSerializable(UUID_ARG) as? UUID
			alarmUUID?.let {
				Log.d(TAG, "update operation")
				isUpdateOperation = true
				presenter.loadAlarm(it)
			}
		}

		return view
	}

	private fun initNoAlarmDependencies(view: View) {
		initDaysListTextView(view)
		initTimeTextView(view)
		initActiveSwitch(view)
		initConfirmButton(view)
	}

	private fun initDaysListTextView(view: View) {
		val textViewIDs: IntArray = intArrayOf(
				R.id.alarm_textview_sun,
				R.id.alarm_textview_mon,
				R.id.alarm_textview_tue,
				R.id.alarm_textview_wed,
				R.id.alarm_textview_thu,
				R.id.alarm_textview_fri,
				R.id.alarm_textview_sat
		)
		daysTextView = Array(7) { index ->
			initializeDayTextView(view, textViewIDs[index], WeekDay.WEEK[index])
		}
	}

	private fun initializeDayTextView(view: View, idTextView: Int, day: Int): TextView {
		val textView = view.findViewById(idTextView) as TextView
		textView.setOnClickListener {
			if (textView.currentTextColor == resources.getColorBasedOnApi23(R.color.grey)) {
				textView.setTextColor(resources.getColorBasedOnApi23(R.color.colorAccent))
				alarmDays = addDay(alarmDays, day)
				Log.d(TAG, "added day ${WeekDay.getShortName(day)}")
			} else {
				textView.setTextColor(resources.getColorBasedOnApi23(R.color.grey))
				alarmDays = removeDay(alarmDays, day)
				Log.d(TAG, "removed day ${WeekDay.getShortName(day)}")
			}
		}

		return textView
	}

	private fun initTimeTextView(view: View) {
		timeTextView = view.findViewById(R.id.alarm_time_picker) as TextView

		timeTextView.text = getHourAndMinuteAsString(alarmHour, alarmMinute)
		timeTextView.setOnClickListener {
			val dialog = TimePickerFragment.newInstance(alarmHour, alarmMinute)
			dialog.setTargetFragment(this, REQUEST_CODE_TIME)
			dialog.show(fragmentManager, DIALOG_TIME)
		}
	}

	private fun initActiveSwitch(view: View) {
		activeSwitch = view.findViewById(R.id.alarm_switch) as Switch
		activeSwitch.isChecked = false
		activeSwitch.setOnClickListener { alarmIsActive = !alarmIsActive }
	}

	private fun initConfirmButton(view: View) {
		confirmButton = view.findViewById(R.id.alarm_confirm_button) as Button
		confirmButton.setOnClickListener {

			val day = Calendar.getInstance()
			day.set(Calendar.HOUR_OF_DAY, alarmHour)
			day.set(Calendar.MINUTE, alarmMinute)
			val alarmToInsert = Alarm(alarmID, day, alarmDays, alarmIsActive)

			if (isUpdateOperation) presenter.updateAlarm(alarmToInsert)
			else presenter.addAlarm(alarmToInsert)
		}
	}

	override fun showAlarm(alarm: Alarm) {
		setAlarmDependencies(alarm)
	}

	private fun setAlarmDependencies(alarm: Alarm) {
		initAlarmVariables(alarm)
		val colorResources = resources
		daysTextView.forEachIndexed { index, textView ->
			setDayTextView(textView, index + 1, colorResources)
		}
		setTimeTextView()
		setActiveSwitch()
	}

	private fun initAlarmVariables(alarm: Alarm) {
		// TODO - What if we use Destructuring Declaration and Component Functions?

		alarmID = alarm.uuid
		alarmHour = alarm.hour
		alarmMinute = alarm.minute
		alarmDays = alarm.days
		alarmIsActive = alarm.active
	}

	private fun setDayTextView(textView: TextView, day: Int, colorResources: Resources) {
		if (hasDay(alarmDays, day)) {
			textView.setTextColor(colorResources.getColorBasedOnApi23(R.color.colorAccent))
		}
	}

	private fun setTimeTextView() {
		timeTextView.text = getHourAndMinuteAsString(alarmHour, alarmMinute)
	}

	private fun setActiveSwitch() {
		activeSwitch.isChecked = alarmIsActive
	}

	override fun confirmAddUpdate() {
		activity.setResult(Activity.RESULT_OK)
		activity.finish()
	}

	override fun showError(message: String?) {
		view?.let {
			Snackbar.make(it, "Error in the operation", Snackbar.LENGTH_SHORT)
					.show()
			Log.d(TAG, message ?: "Error in the operation")
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		presenter.release()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
		if (resultCode != Activity.RESULT_OK) {
			return
		}

		if (requestCode == REQUEST_CODE_TIME) {
			alarmHour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, alarmHour)
			alarmMinute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, alarmMinute)

			timeTextView.text = getHourAndMinuteAsString(alarmHour, alarmMinute)
		}
	}
}
