package com.mariotti.developer.futureclock.controllers.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController
import com.mariotti.developer.futureclock.extensions.getColorBasedOnApi23
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.WeekDay
import com.mariotti.developer.futureclock.util.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class AlarmCreateOrUpdateFragment : Fragment() {

    private var mUUID: UUID = UUID.randomUUID()
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mDays: IntArray? = null
    private var mActive: Boolean = false

    private var mTimeTextView: TextView? = null
    private var mDaysTextView: Array<TextView>? = null

    private var mSwitch: Switch? = null
    private var mConfirmButton: Button? = null

    companion object {
        private val TAG = "AlarmCreateOrUpdateFragment"
        private val DIALOG_TIME = "TimePickerFragment"
        private val UUID_ARG = "UUID_ARG"
        private val REQUEST_CODE_TIME = 1

        fun newInstance(uuid: UUID?): AlarmCreateOrUpdateFragment {
            val fragment = AlarmCreateOrUpdateFragment()
            uuid?.let {
                val args = Bundle()
                args.putSerializable(UUID_ARG, uuid)
                fragment.arguments = args
            }

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_alarm, container, false)

        initNoAlarmDependencies(view)
        initAlarmDependencies()

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
        mDaysTextView = Array(7) { index ->
            initializeDayTextView(view, textViewIDs[index], WeekDay.WEEK[index])
        }
    }

    private fun initializeDayTextView(view: View, idTextView: Int, day: Int): TextView {
        val textView = view.findViewById(idTextView) as TextView
        textView.setOnClickListener {
            if (textView.currentTextColor == resources.getColorBasedOnApi23(R.color.grey)) {
                textView.setTextColor(resources.getColorBasedOnApi23(R.color.colorAccent))
                mDays = addDay(mDays!!, day)
                Log.d(TAG, "added day ${WeekDay.getShortName(day)}")
            } else {
                textView.setTextColor(resources.getColorBasedOnApi23(R.color.grey))
                mDays = removeDay(mDays!!, day)
                Log.d(TAG, "removed day ${WeekDay.getShortName(day)}")
            }
        }

        return textView
    }

    private fun initTimeTextView(view: View) {
        mTimeTextView = view.findViewById(R.id.alarm_time_picker) as TextView

        mTimeTextView!!.text = "00:00"
        mTimeTextView!!.setOnClickListener {
            val dialog = TimePickerFragment.newInstance(mHour, mMinute)
            dialog.setTargetFragment(this, REQUEST_CODE_TIME)
            dialog.show(fragmentManager, DIALOG_TIME)
        }
    }

    private fun initActiveSwitch(view: View) {
        mSwitch = view.findViewById(R.id.alarm_switch) as Switch
        mSwitch!!.isChecked = false
        mSwitch!!.setOnClickListener { mActive = !mActive }
    }

    private fun initConfirmButton(view: View) {
        mConfirmButton = view.findViewById(R.id.alarm_confirm_button) as Button
        mConfirmButton!!.setOnClickListener {
            val day = Calendar.getInstance()
            day.set(Calendar.HOUR_OF_DAY, mHour)
            day.set(Calendar.MINUTE, mMinute)
            val alarmToInsert = Alarm(mUUID, day, mDays!!, mActive)
            val databaseController = DatabaseAlarmController.getInstance(activity)

            val observable: Observable<Unit>
            if (!checkIfUpdateOperation()) observable = databaseController.addAlarm(alarmToInsert)
            else observable = databaseController.updateAlarm(alarmToInsert)

            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<Unit>() {
                        override fun onNext(p0: Unit?) {
                            activity.setResult(Activity.RESULT_OK)
                            activity.finish()
                        }

                        override fun onError(p0: Throwable?) {
                            Log.d(TAG, "onError confirmButton")
                        }

                        override fun onCompleted() {
                            Log.d(TAG, "onCompleted")
                        }

                    })
        }
    }

    private fun initAlarmDependencies() {
        if (checkIfUpdateOperation()) {
            val alarmUUID = arguments.getSerializable(UUID_ARG) as UUID
            Log.d(TAG, "UUID is $alarmUUID")
            DatabaseAlarmController.getInstance(activity)
                    .getAlarm(alarmUUID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val alarm = it ?: getDefaultAlarmInstance()
                        setAlarmDependencies(alarm)
                    }
        } else {
            val alarm: Alarm = getDefaultAlarmInstance()
            setAlarmDependencies(alarm)
        }
    }

    private fun checkIfUpdateOperation(): Boolean {
        return arguments != null && arguments.getSerializable(UUID_ARG) != null
    }

    private fun setAlarmDependencies(alarm: Alarm) {
        initAlarmVariables(alarm)
        mDaysTextView!!.forEachIndexed { index, textView -> setDayTextView(textView, index + 1) }
        setTimeTextView()
        setActiveSwitch()
    }

    private fun initAlarmVariables(alarm: Alarm) {
        mUUID = alarm.uuid
        mHour = alarm.hour
        mMinute = alarm.minute
        mDays = alarm.days
        mActive = alarm.active
    }

    private fun setDayTextView(textView: TextView, day: Int) {
        if (hasDay(mDays!!, day)) {
            textView.setTextColor(resources.getColorBasedOnApi23(R.color.colorAccent))
        }
    }

    private fun setTimeTextView() {
        mTimeTextView!!.text = getHourAndMinuteAsString(mHour, mMinute)
    }

    private fun setActiveSwitch() {
        mSwitch!!.isChecked = mActive
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_TIME) {
            mHour = data!!.getIntExtra(TimePickerFragment.EXTRA_HOUR, mHour)
            mMinute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, mMinute)

            mTimeTextView!!.text = getHourAndMinuteAsString(mHour, mMinute)
        }
    }
}
