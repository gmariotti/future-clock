package com.mariotti.developer.futureclock.controllers.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.mariotti.developer.futureclock.R;
import com.mariotti.developer.futureclock.controllers.DatabaseAlarmController;
import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.util.AlarmUtil;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    private AdapterFragment mFragment;
    private List<Alarm> mAlarms;

    public AlarmAdapter(AdapterFragment fragment, List<Alarm> alarms) {
        mFragment = fragment;
        mAlarms = alarms;
    }

    @Override
    public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mFragment.getActivity());
        View view = layoutInflater.inflate(R.layout.list_item_alarm, parent, false);

        return new AlarmHolder(view, this);
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, int position) {
        Alarm alarm = mAlarms.get(position);
        holder.bindAlarm(alarm);
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public void setAlarms(List<Alarm> alarms) {
        mAlarms = alarms;
    }

    /*********************
     * ViewHolder definition
     *********************/
    public class AlarmHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private AlarmAdapter mAdapter;

        private TextView mTimeTextView;
        private TextView mDaysTextView;
        private Switch mActiveSwitch;

        private Alarm mAlarm;

        public AlarmHolder(View itemView, AlarmAdapter alarmAdapter) {
            super(itemView);
            mAdapter = alarmAdapter;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_time);
            mDaysTextView = (TextView) itemView.findViewById(R.id.list_item_days);
            mActiveSwitch = (Switch) itemView.findViewById(R.id.list_item_switch);
        }

        public void bindAlarm(final Alarm alarm) {
            mAlarm = alarm;
            mTimeTextView.setText(AlarmUtil.getHourAndMinuteAsString(mAlarm.getHour(), mAlarm.getMinute()));
            mDaysTextView.setText(AlarmUtil.getShortDaysString(mAlarm));
            mActiveSwitch.setChecked(mAlarm.getActive());

            mActiveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mAlarm.setActive(isChecked);
                try {
                    DatabaseAlarmController.getDatabaseAlarmController(mAdapter.mFragment.getActivity())
                            .updateAlarm(alarm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onClick(View v) {
            mAdapter.mFragment.modifyAlarm(mAlarm.getUuid());
        }

        @Override
        public boolean onLongClick(View v) {
            // show a dialog to delete the current alarm
            mAdapter.mFragment.deleteAlarm(mAlarm);

            return true;
        }
    }
}