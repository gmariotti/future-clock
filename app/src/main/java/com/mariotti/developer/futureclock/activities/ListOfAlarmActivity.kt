package com.mariotti.developer.futureclock.activities

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.mariotti.developer.futureclock.controllers.fragments.ListOfAlarmFragment

class ListOfAlarmActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return ListOfAlarmFragment.newInstance()
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, ListOfAlarmActivity::class.java)
        }
    }
}
