package com.mariotti.developer.futureclock.controllers;

import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.WeekDay;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class AlarmManagementControllerTest {
    List<Alarm> mAlarms = new ArrayList<>();
    Calendar today;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy z");

    @Before
    public void init() {
        today = Calendar.getInstance();
        String timezone = today.getTimeZone().getDisplayName();
        Alarm alarm1 = new Alarm(UUID.randomUUID(), 18, 7,
                new int[]{WeekDay.INSTANCE.getMONDAY(), WeekDay.INSTANCE.getFRIDAY()},
                timezone, true);
        Alarm alarm2 = new Alarm(UUID.randomUUID(), 10, 34,
                new int[]{WeekDay.INSTANCE.getSATURDAY(), WeekDay.INSTANCE.getSUNDAY()},
                timezone, true);
        Alarm alarm3 = new Alarm(UUID.randomUUID(), 5, 11,
                new int[]{WeekDay.INSTANCE.getTHURSDAY()},
                timezone, true);
        mAlarms.add(alarm1);
        mAlarms.add(alarm2);
        mAlarms.add(alarm3);
    }

    @Test
    public void getNextAlarmNotNullTest() {
        Alarm alarm = AlarmManagementController.getNextAlarm(mAlarms);
        assertNotNull(alarm);
        System.out.println(alarm.toString());
    }

    @Test
    public void getNearestDayForAlarmNotNullTest() {
        for (Alarm alarm : mAlarms) {
            Calendar day = AlarmManagementController.getNearestDayForAlarm(alarm, today);
            assertNotNull(day);
            System.out.println(mSimpleDateFormat.format(day.getTime()));
        }
    }
}
