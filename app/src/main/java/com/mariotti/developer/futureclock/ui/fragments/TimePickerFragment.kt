package com.mariotti.developer.futureclock.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.TimePicker

class TimePickerFragment private constructor() : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    companion object {
        val EXTRA_HOUR = "com.mariotti.developer.futureclock.controller.hour"
        val EXTRA_MINUTE = "com.mariotti.developer.futureclock.controller.minute"

        private val TAG = "TimePickerFragment"
        private val ARG_HOUR = "ARG_HOUR"
        private val ARG_MINUTE = "ARG_MINUTE"

        fun newInstance(hour: Int, minute: Int): TimePickerFragment {
            val fragment = TimePickerFragment()
            val args = Bundle()
            args.putInt(ARG_HOUR, hour % 24)
            args.putInt(ARG_MINUTE, minute % 60)
            fragment.arguments = args

            Log.d(TAG, "newInstance")

            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments
        val hour = args.getInt(ARG_HOUR)
        val minute = args.getInt(ARG_MINUTE)

        val dialog = TimePickerDialog(activity, this, hour, minute, true)

        return dialog
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        targetFragment?.let {
            val intent = Intent()
            intent.putExtra(EXTRA_HOUR, hourOfDay)
            intent.putExtra(EXTRA_MINUTE, minute)

            it.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
        }
    }
}
