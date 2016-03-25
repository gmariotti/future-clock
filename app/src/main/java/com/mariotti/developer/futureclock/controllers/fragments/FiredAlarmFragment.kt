package com.mariotti.developer.futureclock.controllers.fragments

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.OpenMapWeatherFetchr
import com.mariotti.developer.futureclock.controllers.RxDatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.getNextAlarm
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.OpenMapWeather
import com.mariotti.developer.futureclock.util.getHourAndMinuteAsString
import com.mariotti.developer.futureclock.util.makeNotificationFromAlarm
import rx.Single
import rx.SingleSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.util.*

class FiredAlarmFragment : Fragment() {

    private var mTextToSpeech: TextToSpeech? = null

    private var mAlarmFiredTextView: TextView? = null
    private var mButton: Button? = null

    companion object {
        private val TAG = "FiredAlarmFragment"
        private val ARG_UUID = "ARG_UUID"

        fun newInstance(uuid: UUID?): FiredAlarmFragment {
            val fragment = FiredAlarmFragment()
            if (uuid != null) {
                val args = Bundle()
                args.putSerializable(ARG_UUID, uuid)
                fragment.arguments = args
            }

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View {
        val view = inflater.inflate(R.layout.fragment_alarm_fired, container, false)

        mAlarmFiredTextView = view.findViewById(R.id.alarm_fired_text_view) as TextView
        mButton = view.findViewById(R.id.button_talk) as Button

        val uuid = arguments.getSerializable(ARG_UUID) as UUID
        manageAlarmFired(uuid)

        mButton!!.setOnClickListener {
            Single.create(Single.OnSubscribe<com.mariotti.developer.futureclock.models.OpenMapWeather> { singleSubscriber ->
                try {
                    singleSubscriber.onSuccess(OpenMapWeatherFetchr.parseOpenMapWeather())
                } catch (e: IOException) {
                    Snackbar.make(getView()!!, "Weather Error IOException", Snackbar.LENGTH_LONG)
                            .show()
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleSubscriber<OpenMapWeather>() {
                        override fun onSuccess(openMapWeather: OpenMapWeather) {
                            mTextToSpeech = TextToSpeech(activity,
                                    TextToSpeech.OnInitListener {
                                        val goodMorning = "Good morning Sir, "
                                        mTextToSpeech!!.speak(
                                                StringBuilder().append(goodMorning)
                                                        .append(openMapWeather.toString())
                                                        .toString(),
                                                TextToSpeech.QUEUE_FLUSH,
                                                null)
                                        while (mTextToSpeech!!.isSpeaking) {
                                            // Just wait the end of speaking before shutdown the TextToSpeech
                                        }
                                        mTextToSpeech!!.stop()
                                        mTextToSpeech!!.shutdown()
                                    })
                        }

                        override fun onError(error: Throwable) {
                            Snackbar.make(getView()!!, "Weather Error onError", Snackbar.LENGTH_LONG)
                                    .show()
                        }
                    })
        }

        return view
    }

    private fun manageAlarmFired(uuid: UUID) {
        RxDatabaseAlarmController.getInstance(activity)
                .getAlarm(uuid)
                .flatMap { alarmFound: Alarm? ->
                    Single.create<Pair<Alarm?, Alarm?>> {
                        val dbController = DatabaseAlarmController.getInstance(context)
                        alarmFound?.let {
                            if (alarmFound.days.size == 0) {
                                dbController.updateAlarm(it.copy(active = false))
                            }
                        }
                        Pair(alarmFound, getNextAlarm(dbController.getActiveAlarms()))
                    }.subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<Pair<Alarm?, Alarm?>>() {
                    override fun onSuccess(pair: Pair<Alarm?, Alarm?>) {
                        val alarmFound: Alarm? = pair.first
                        alarmFound?.let {
                            mAlarmFiredTextView!!.text = "Fired alarm at time " +
                                    "${getHourAndMinuteAsString(alarmFound.hour, alarmFound.minute)}"
                        }
                        pair.second?.let {
                            setNextAlarmToFire(it)
                        }
                    }

                    override fun onError(error: Throwable) {
                        Log.d(TAG, "onError finding alarm")
                    }
                })
    }

    private fun setNextAlarmToFire(alarm: Alarm) {
        makeNotificationFromAlarm(context, alarm)
    }
}
