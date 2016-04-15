package com.mariotti.developer.futureclock.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.mariotti.developer.futureclock.R
import com.mariotti.developer.futureclock.activities.FiredAlarmActivity
import com.mariotti.developer.futureclock.controllers.RxDatabaseAlarmController
import com.mariotti.developer.futureclock.controllers.getNextAlarm
import com.mariotti.developer.futureclock.models.Alarm
import com.mariotti.developer.futureclock.models.AlarmRepositoryImpl
import com.mariotti.developer.futureclock.presenters.ListOfAlarmPresenter
import com.mariotti.developer.futureclock.presenters.ListOfAlarmPresenterImpl
import com.mariotti.developer.futureclock.ui.activities.AlarmCreateOrUpdateActivity
import com.mariotti.developer.futureclock.ui.adapters.ListOfAlarmAdapter
import java.util.*

class ListOfAlarmFragment : Fragment(), MainScreen {

	lateinit private var createAlarmFab: FloatingActionButton
	lateinit private var alarmListRecyclerView: RecyclerView
	lateinit private var recyclerViewAdapter: ListOfAlarmAdapter
	private val listOfAlarmPresenter: ListOfAlarmPresenter by lazy {
		ListOfAlarmPresenterImpl(this, AlarmRepositoryImpl.getInstance(activity.applicationContext))
	}

	// TODO - find a better solution
	private var mAlarmToFireID: UUID = UUID(0L, 0L)

	companion object {
		private val TAG = "ListOfAlarmFragment"
		private val REQUEST_CODE_ALARM_MANAGEMENT = 1
		private val UUID_NEXT_ALARM = "UUID_NEXT_ALARM"

		fun newInstance(): ListOfAlarmFragment {
			return ListOfAlarmFragment()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		recyclerViewAdapter = ListOfAlarmAdapter(listOfAlarmPresenter, listOf<Alarm>())
		setHasOptionsMenu(true)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_future_clock, container, false)

		createAlarmFab = view.findViewById(R.id.alarm_fab) as FloatingActionButton
		createAlarmFab.setOnClickListener {
			createUpdateRequest()
		}

		alarmListRecyclerView = view.findViewById(R.id.alarms_recycler_view) as RecyclerView
		alarmListRecyclerView.layoutManager = LinearLayoutManager(activity)
		alarmListRecyclerView.adapter = recyclerViewAdapter

		listOfAlarmPresenter.loadAlarms()

		return view
	}

	override fun createUpdateRequest(alarmID: UUID?) {
		val intent = AlarmCreateOrUpdateActivity.newIntent(activity, alarmID)
		startActivityForResult(intent, REQUEST_CODE_ALARM_MANAGEMENT)
	}

	override fun showAlarms(alarms: List<Alarm>) {
		recyclerViewAdapter.setAlarms(alarms)
	}

	override fun onDestroy() {
		super.onDestroy()
		listOfAlarmPresenter.release()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putSerializable(UUID_NEXT_ALARM, mAlarmToFireID)

		super.onSaveInstanceState(outState)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode != Activity.RESULT_OK) {
			return
		}

		when (requestCode) {
			REQUEST_CODE_ALARM_MANAGEMENT -> {
				listOfAlarmPresenter.loadAlarms()
				//updateNextAlarmToFire()
			}
		}
	}

	private fun updateNextAlarmToFire() {
		RxDatabaseAlarmController.getInstance(context)
				.getActiveAlarms()
				.subscribe {
					val alarmToFire = getNextAlarm(it)
					if (alarmToFire != null) {
						if (!alarmToFire.uuid.equals(mAlarmToFireID)) {
							mAlarmToFireID = alarmToFire.uuid
							FiredAlarmActivity.setActiveAlarm(activity, mAlarmToFireID)
						}
					} else {
						FiredAlarmActivity.removeAlarmIfSet(activity)
					}
				}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.app_menu, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.menu_settings -> {
				Snackbar.make(view!!, "Settings", Snackbar.LENGTH_LONG)
						.show()
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}
}
