package com.mariotti.developer.futureclock.controllers.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
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
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.WeekDay
import com.mariotti.developer.futureclock.util.addDay
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.hasDay
import com.mariotti.developer.futureclock.util.removeDay
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.observable
import rx.lang.kotlin.subscriber
import rx.schedulers.Schedulers
import java.util.*

class AlarmCreateOrUpdateFragment : Fragment() {

    private var mUUID: UUID? = null
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mDays: IntArray? = null
    private var mActive: Boolean = false

    private var mTimeTextView: TextView? = null
    private var mDaysTextView: Array<TextView>? = null

    private var mSwitch: Switch? = null
    private var mConfirmButton: Button? = null

    companion object {
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
        Log.d(tag, "onCreateView is started")

        observable<Alarm> {
            subscriber<Alarm>().onNext(getAlarm())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    initAlarmVariables(it)
                    initDaysListTextView(view)
                    initTimeTextView(view)
                    initActiveSwitch(view)
                    initConfirmButton(view)

                    Log.d(tag, "subscribe is ended")
                }

        Log.d(tag, "onCreateView is ended")
        return view
    }

    private fun getAlarm(): Alarm {
        var alarmToUpdate: Alarm? = null

        if (checkIfUpdateOperation()) {
            val alarmUUID = arguments.getSerializable(UUID_ARG) as UUID
            alarmToUpdate = DatabaseAlarmController.getInstance(activity).getAlarm(alarmUUID)
        }

        alarmToUpdate = alarmToUpdate ?: Alarm(UUID.randomUUID(), Calendar.getInstance(), intArrayOf(), false)

        return alarmToUpdate
    }

    private fun checkIfUpdateOperation(): Boolean {
        Log.d(tag, "checkIfUpdateOperation")
        return arguments != null && arguments.getSerializable(UUID_ARG) != null
    }

    private fun initAlarmVariables(alarm: Alarm) {
        mUUID = alarm.uuid
        mHour = alarm.hour
        mMinute = alarm.minute
        mDays = alarm.days
        mActive = alarm.active
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
        textView.setOnClickListener { v -> // Compatibility with version before Android M - API 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (textView.currentTextColor == resources.getColor(R.color.grey, null)) {
                    textView.setTextColor(resources.getColor(R.color.colorAccent, null))
                    mDays = addDay(mDays!!, day)
                } else {
                    textView.setTextColor(resources.getColor(R.color.grey, null))
                    mDays = removeDay(mDays!!, day)
                }
            } else {
                if (textView.currentTextColor == resources.getColor(R.color.grey)) {
                    textView.setTextColor(resources.getColor(R.color.colorAccent))
                    mDays = addDay(mDays!!, day)
                } else {
                    textView.setTextColor(resources.getColor(R.color.grey))
                    mDays = removeDay(mDays!!, day)
                }
            }
        }

        if (hasDay(mDays!!, day)) {
            // Compatibility with version before Android M - API 23
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(resources.getColor(R.color.colorAccent, null))
            } else {
                textView.setTextColor(resources.getColor(R.color.colorAccent))
            }
        }

        return textView
    }

    private fun initTimeTextView(view: View) {
        mTimeTextView = view.findViewById(R.id.alarm_time_picker) as TextView

        Log.d(tag, "TimeTextView is null = ${mTimeTextView == null}")
        mTimeTextView!!.text = getHourAndMinuteAsString(mHour, mMinute)
        mTimeTextView!!.setOnClickListener { v ->
            val dialog = TimePickerFragment.newInstance(mHour, mMinute)
            dialog.setTargetFragment(this, REQUEST_CODE_TIME)
            dialog.show(fragmentManager, DIALOG_TIME)
        }
    }

    private fun initActiveSwitch(view: View) {
        mSwitch = view.findViewById(R.id.alarm_switch) as Switch
        mSwitch!!.isChecked = mActive
        mSwitch!!.setOnClickListener { v -> mActive = !mActive }
    }

    private fun initConfirmButton(view: View) {
        mConfirmButton = view.findViewById(R.id.alarm_confirm_button) as Button
        mConfirmButton!!.setOnClickListener { view ->
            observable<Boolean> {
                val day = Calendar.getInstance()
                day.set(Calendar.HOUR_OF_DAY, mHour)
                day.set(Calendar.MINUTE, mMinute)
                val alarmToInsert = Alarm(mUUID!!, day, mDays!!, mActive)
                val databaseController = DatabaseAlarmController.getInstance(activity)
                val returnValue: Boolean
                try {
                    if (!checkIfUpdateOperation()) databaseController.addAlarm(alarmToInsert)
                    else databaseController.updateAlarm(alarmToInsert)
                    returnValue = true
                } catch(e: Exception) {
                    returnValue = false
                }
                subscriber<Boolean>().onNext(returnValue)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it) {
                            this.activity.setResult(Activity.RESULT_OK)
                            this.activity.finish()
                        } else {
                            // TODO - error snackbar
                        }
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
