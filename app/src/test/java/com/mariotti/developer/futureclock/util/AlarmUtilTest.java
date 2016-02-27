package com.mariotti.developer.futureclock.util;

import com.mariotti.developer.futureclock.models.Alarm;
import com.mariotti.developer.futureclock.models.WeekDay;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlarmUtilTest {
    Calendar today;
    Alarm mAlarm;

    @Before
    public void init() {
        today = Calendar.getInstance();
        mAlarm = new Alarm(
                UUID.randomUUID(),
                today,
                new int[]{
                        WeekDay.INSTANCE.getSUNDAY(),
                        WeekDay.INSTANCE.getMONDAY(),
                        WeekDay.INSTANCE.getTHURSDAY(),
                        WeekDay.INSTANCE.getFRIDAY()
                },
                false
        );
    }

    @Test
    public void alarmDaysStringTest() {
        StringBuilder expected = new StringBuilder();
        expected.append(WeekDay.INSTANCE.getShortName(WeekDay.INSTANCE.getSUNDAY()));
        expected.append(", ");
        expected.append(WeekDay.INSTANCE.getShortName(WeekDay.INSTANCE.getMONDAY()));
        expected.append(", ");
        expected.append(WeekDay.INSTANCE.getShortName(WeekDay.INSTANCE.getTHURSDAY()));
        expected.append(", ");
        expected.append(WeekDay.INSTANCE.getShortName(WeekDay.INSTANCE.getFRIDAY()));

        assertEquals(expected.toString(), AlarmUtil.getShortDaysString(mAlarm));
    }

    @Test
    public void alarmTimeStringTest() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        assertEquals(simpleDateFormat.format(today.getTime()),
                AlarmUtil.getHourAndMinuteAsString(mAlarm.getHour(), mAlarm.getMinute()));
    }

    @Test
    public void alarmAddDayTest() {
        int days[] = mAlarm.getDays();
        int dayToAdd = WeekDay.INSTANCE.getWEDNESDAY();
        int daysExpected[] = new int[]{
                WeekDay.INSTANCE.getSUNDAY(),
                WeekDay.INSTANCE.getMONDAY(),
                WeekDay.INSTANCE.getWEDNESDAY(),
                WeekDay.INSTANCE.getTHURSDAY(),
                WeekDay.INSTANCE.getFRIDAY()
        };

        assertArrayEquals(daysExpected, AlarmUtil.addDay(days, dayToAdd));
    }

    @Test
    public void alarmRemoveDayTest() {
        int days[] = mAlarm.getDays();
        int dayToRemove = WeekDay.INSTANCE.getSUNDAY();
        int daysExpected[] = new int[]{
                WeekDay.INSTANCE.getMONDAY(),
                WeekDay.INSTANCE.getTHURSDAY(),
                WeekDay.INSTANCE.getFRIDAY()
        };

        assertArrayEquals(daysExpected, AlarmUtil.removeDay(days, dayToRemove));
    }

    @Test
    public void alarmContainsDayTest() {
        assertTrue(AlarmUtil.hasDay(mAlarm.getDays(), WeekDay.INSTANCE.getFRIDAY()));
    }
}
