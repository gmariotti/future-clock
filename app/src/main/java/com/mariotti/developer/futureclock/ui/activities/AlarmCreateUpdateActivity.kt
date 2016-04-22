package com.mariotti.developer.futureclock.ui.activities

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.extensions.getColorBasedOnApi23
import com.mariotti.developer.futureclock.kotterknife.bindView
import com.mariotti.developer.futureclock.kotterknife.bindViews
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.AlarmRepositoryImpl
import com.mariotti.developer.futureclock.models.HourMinuteAndTimeZone
import com.mariotti.developer.futureclock.models.WeekDay
import com.mariotti.developer.futureclock.presenters.AddUpdateAlarmPresenter
import com.mariotti.developer.futureclock.presenters.AddUpdateAlarmPresenterImpl
import com.mariotti.developer.futureclock.ui.activities.AddUpdateAlarmScreen
import com.mariotti.developer.futureclock.util.addDay
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.hasDay
import com.mariotti.developer.futureclock.util.removeDay
import java.util.*

class AlarmCreateUpdateActivity : BaseActivity(), AddUpdateAlarmScreen {

	private var alarmID: UUID = UUID.randomUUID()
	private var alarmHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
	private var alarmMinute: Int = Calendar.getInstance().get(Calendar.MINUTE)
	private var alarmDays: IntArray = intArrayOf()
	private var alarmIsActive: Boolean = false
	private var isUpdateOperation: Boolean = false

	private val timeTextView: TextView by bindView<TextView>(R.id.alarm_time_picker)
	private val activeSwitch: Switch by bindView<Switch>(R.id.alarm_switch)
	private val confirmButton: Button by bindView<Button>(R.id.alarm_confirm_button)
	private val daysTextView: List<TextView> by bindViews(
			R.id.alarm_textview_sun, R.id.alarm_textview_mon, R.id.alarm_textview_tue,
			R.id.alarm_textview_wed, R.id.alarm_textview_thu, R.id.alarm_textview_fri,
			R.id.alarm_textview_sat)
	private val presenter: AddUpdateAlarmPresenter by lazy {
		Log.d(TAG, "AddUpdateAlarmPresenter by lazy")
		AddUpdateAlarmPresenterImpl(this, AlarmRepositoryImpl.getInstance(this))
	}

	companion object {
		private val TAG = "AlarmCreateUpdateActivity"
		private val EXTRA_ALARM_UUID =
				"com.mariotti.developer.futureclock.ui.activities.alarm_uuid"

		fun newIntent(packageContext: Context, alarmUUID: UUID?): Intent {
			val intent = Intent(packageContext, AlarmCreateUpdateActivity::class.java)

			alarmUUID?.let {
				intent.putExtra(EXTRA_ALARM_UUID, alarmUUID)
				Log.d(TAG, "UUID is $alarmUUID")
			}

			return intent
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val uuid = intent.getSerializableExtra(EXTRA_ALARM_UUID) as UUID?
		setContentView(R.layout.activity_alarm_management)

		initDaysListTextView()
		initTimeTextView()
		initActiveSwitch()
		initConfirmButton()

		uuid?.let {
			Log.d(TAG, "update operation")
			isUpdateOperation = true
			presenter.loadAlarm(it)
		}
	}

	private fun initDaysListTextView() {
		val textViewIDs: IntArray = intArrayOf(R.id.alarm_textview_sun, R.id.alarm_textview_mon,
				R.id.alarm_textview_tue, R.id.alarm_textview_wed, R.id.alarm_textview_thu,
				R.id.alarm_textview_fri, R.id.alarm_textview_sat)
		textViewIDs.forEachIndexed { index, id ->
			initializeDayTextView(daysTextView[index], WeekDay.WEEK[index])
		}
	}

	private fun initializeDayTextView(textView: TextView, day: Int): TextView {
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

	private fun initTimeTextView() {
		timeTextView.text = getHourAndMinuteAsString(alarmHour, alarmMinute)
		timeTextView.setOnClickListener {
			val dialog = TimePickerDialog(
					this,
					TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
						alarmHour = hourOfDay
						alarmMinute = minute
						timeTextView.text = getHourAndMinuteAsString(alarmHour, alarmMinute)
					}, alarmHour, alarmMinute, true)
			dialog.show()
		}
	}

	private fun initActiveSwitch() {
		//		activeSwitch = view.findViewById(R.id.alarm_switch) as Switch
		activeSwitch.isChecked = false
		activeSwitch.setOnClickListener { alarmIsActive = !alarmIsActive }
	}

	private fun initConfirmButton() {
		confirmButton.setOnClickListener {
			val time = HourMinuteAndTimeZone.getHourMinuteAndTimeZone(alarmHour, alarmMinute,
					TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT))
			val alarmToInsert = Alarm(alarmID, time, alarmDays, alarmIsActive)

			if (isUpdateOperation) presenter.updateAlarm(alarmToInsert)
			else presenter.addAlarm(alarmToInsert)
		}
	}

	override fun showAlarm(alarm: Alarm) {
		setAlarmDependencies(alarm)
	}

	private fun setAlarmDependencies(alarm: Alarm) {
		initAlarmDependencies(alarm)
		val colorResources = resources
		daysTextView.forEachIndexed { index, textView -> setDayTextView(textView, index + 1, colorResources) }
		timeTextView.text = getHourAndMinuteAsString(alarmHour, alarmMinute)
		activeSwitch.isChecked = alarmIsActive
	}

	private fun initAlarmDependencies(alarm: Alarm) {
		// TODO - What if we use Destructuring Declaration and Component Functions?

		alarmID = alarm.uuid
		alarmHour = alarm.getHour()
		alarmMinute = alarm.getMinute()
		alarmDays = alarm.days
		alarmIsActive = alarm.active
	}

	private fun setDayTextView(textView: TextView, day: Int, colorResources: Resources) {
		if (hasDay(alarmDays, day)) {
			textView.setTextColor(colorResources.getColorBasedOnApi23(R.color.colorAccent))
		}
	}

	override fun confirmAddUpdate() {
		this.setResult(Activity.RESULT_OK)
		this.finish()
	}

	override fun showError(message: String?) {
		Log.d(TAG, "Error in the operation")
	}

	override fun onDestroy() {
		super.onDestroy()
		presenter.release()
	}
}
