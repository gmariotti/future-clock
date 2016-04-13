package com.mariotti.developer.futureclock.ui.activities

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.mariotti.developer.futureclock.ui.activities.SingleFragmentActivity

import com.mariotti.developer.futureclock.ui.fragments.ListOfAlarmFragment

class MainActivity : SingleFragmentActivity() {

	override fun createFragment(): Fragment {
		return ListOfAlarmFragment.newInstance()
	}

	companion object {

		// TODO -> consider deletion
		fun newIntent(context: Context): Intent {
			return Intent(context, MainActivity::class.java)
		}
	}
}
