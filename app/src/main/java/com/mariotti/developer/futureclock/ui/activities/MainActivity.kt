package com.mariotti.developer.futureclock.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.kotterknife.bindView
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.AlarmRepositoryImpl
import com.mariotti.developer.futureclock.presenters.ListOfAlarmPresenter
import com.mariotti.developer.futureclock.presenters.ListOfAlarmPresenterImpl
import com.mariotti.developer.futureclock.ui.adapters.ListOfAlarmAdapter
import com.mariotti.developer.futureclock.ui.activities.MainScreen
import java.util.*

class MainActivity : BaseActivity(), MainScreen {
	private val createAlarmFab: FloatingActionButton by bindView(R.id.alarm_fab)
	private val alarmListRecyclerView: RecyclerView by bindView(R.id.alarms_recycler_view)
	private val recyclerViewAdapter: ListOfAlarmAdapter by lazy {
		ListOfAlarmAdapter(listOfAlarmPresenter, listOf<Alarm>())
	}
	private val listOfAlarmPresenter: ListOfAlarmPresenter by lazy {
		Log.d(MainActivity.TAG, "listOfAlarmPresenter by lazy")
		ListOfAlarmPresenterImpl(this, AlarmRepositoryImpl.getInstance(this))
	}

	companion object {
		private val TAG = "MainActivity"
		private val REQUEST_CODE_ALARM_MANAGEMENT = 1
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.main_layout)
		createAlarmFab.setOnClickListener { createUpdateRequest() }
		alarmListRecyclerView.layoutManager = LinearLayoutManager(this)
		alarmListRecyclerView.adapter = recyclerViewAdapter
	}

	override fun createUpdateRequest(alarmID: UUID?) {
		val intent = AlarmCreateUpdateActivity.newIntent(this, alarmID)
		startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT)
	}

	override fun onDestroy() {
		super.onDestroy()
		listOfAlarmPresenter.release()
	}

	override fun showAlarms(alarms: List<Alarm>) {
		recyclerViewAdapter.setAlarms(alarms)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode != Activity.RESULT_OK) return

		when (requestCode) {
			REQUEST_CODE_ALARM_MANAGEMENT -> {
				listOfAlarmPresenter.loadAlarms()
//				updateNextAlarmToFire
			}
		}
	}

	private fun updateNextAlarmToFire() {
		throw UnsupportedOperationException()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		super.onCreateOptionsMenu(menu)
		this.menuInflater.inflate(R.menu.app_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.menu_settings -> {
				// TODO
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}
}
