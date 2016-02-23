package com.mariotti.developer.futureclock.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import com.mariotti.developer.futureclock.models.Alarm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AlarmUtilTest {

    @Test
    public void testAlarmTimeString() {
        try {
            Calendar today = Calendar.getInstance();
            int days[] = new int[]{1, 2};
            Alarm alarm = new Alarm(UUID.randomUUID(), today, days, false);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            assertEquals(simpleDateFormat.format(today.getTime()),
                    AlarmUtil.getHourAndMinuteAsString(alarm.getHour(), alarm.getMinute()));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}
